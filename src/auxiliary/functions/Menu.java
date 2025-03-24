package auxiliary.functions;

import arc.Core;
import arc.Events;
import arc.KeyBinds;
import arc.graphics.Color;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.*;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.ObjectIntMap;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Strings;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.*;
import static arc.Core.keybinds;
import static auxiliary.binding.KeyBind.isOpen;
import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.mobile;

public class Menu extends Dialog {
    Table table = new Table();
    ImageButton button = new ImageButton(Icon.menu);
    public static BaseDialog dialog = new BaseDialog("功能面板");
    public static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

    protected KeyBinds.Section section;
    protected boolean rebindAxis = false;
    protected boolean rebindMin = true;
    protected float scroll;
    protected ObjectIntMap<KeyBinds.Section> sectionControls = new ObjectIntMap<>();

    Dialog testDialog = new Dialog();

    public Menu() {
        Events.run(EventType.Trigger.update, () -> table.visible = !(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()));

        setDialog(dialog);
        //new
        setup();

        table.add(button).row();
        if (mobile) {
            ImageButton androidButton = getImageButton();
            table.add(androidButton);
        }

        button.clicked(() -> {
            if (!isDragged) dialog.show();
        });

        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            t.add(table).row();
            t.right();
            //new
            t.button("00", () -> {
                testDialog.show();
            });
        });
        table.addListener(new DragListener(table));
    }

    private static ImageButton getImageButton() {
        ImageButton.ImageButtonStyle style = new ImageButton.ImageButtonStyle() {
            {
                this.checked = Tex.buttonDown;
                this.down = Tex.buttonDown;
                this.up = Tex.button;
                this.over = Tex.buttonOver;
                this.disabled = Tex.buttonDisabled;
            }
        };
        ImageButton androidButton = new ImageButton(Icon.android, style);
        androidButton.clicked(() -> isOpen = !isOpen);
        return androidButton;
    }

    public void setDialog(BaseDialog dialog) {
        functions.addAll(new SpeedChange(), new SunLight(), new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove(), new Launch());

        int width = Core.graphics.getWidth() / 4;
        int height = Core.graphics.getHeight() - 64;

        dialog.cont.table(main -> {
            main.defaults().growX().fillX().margin(0).pad(0);

            main.table(list -> {
                for (Function function : functions) {
                    list.add(function.getName()).height(50).row();
                }
            }).size(width / 2f, height);

            main.table(actions -> {
                for (Function function : functions) {
                    if (function.getButtonID() == 0) {
                        actions.button("使用", function::onClick).size(100, 50).row();
                    } else if (function.getButtonID() == 1) {
                        actions.table(sliderTable -> {
                            sliderTable.defaults().growX().fillX();

                            sliderTable.add("开").width(20f).color(Color.green);

                            sliderTable.add(function.slider).growX().height(50f).padLeft(20f).padRight(20).margin(0);

                            sliderTable.add("关").width(20f).color(Color.red);
                        }).growX().height(50f).row();
                    } else if (function.getButtonID() == 2) {
                        actions.table(speedTable -> {
                            speedTable.defaults().growX().fillX().margin(0).pad(0);

                            Label speedLabel = new Label("1x");
                            speedLabel.setColor(Pal.accent);
                            speedTable.add(speedLabel).width(20f).left().margin(0).pad(0);

                            float[] speedValues = {1f, 2f, 5f, 10f, 20f};
                            speedTable.slider(0, speedValues.length - 1, 1, 0, value -> {
                                float selectedSpeed = speedValues[(int) value];
                                Time.setDeltaProvider(() -> Math.min(Core.graphics.getDeltaTime() * 60.0f * selectedSpeed, 3.0f));
                                speedLabel.setText((int) selectedSpeed + "x");
                            }).growX().height(50f).margin(0).padLeft(10);
                        }).growX().height(50f).margin(0).pad(0).row();
                    }
                }
            }).size(width / 2f, height);
        }).size(width, height);

        dialog.addCloseButton();
    }

    private void setup() {
        cont.clear();

        KeyBinds.Section[] sections = Core.keybinds.getSections();

        Stack stack = new Stack();
        ButtonGroup<TextButton> group = new ButtonGroup<>();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);
        pane.update(() -> scroll = pane.getScrollY());
        this.section = sections[0];

        for (KeyBinds.Section section : sections) {
            if (!sectionControls.containsKey(section))
                sectionControls.put(section, input.getDevices().indexOf(section.device, true));

            if (sectionControls.get(section, 0) >= input.getDevices().size) {
                sectionControls.put(section, 0);
                section.device = input.getDevices().get(0);
            }

            if (sections.length != 1) {
                TextButton button = new TextButton(bundle.get("section." + section.name + ".name", Strings.capitalize(section.name)));
                if (section.equals(this.section))
                    button.toggle();

                button.clicked(() -> this.section = section);

                group.add(button);
                cont.add(button).fill();
            }

            Table table = new Table();

            Label device = new Label("Keyboard");
            //device.setColor(style.controllerColor);
            device.setAlignment(Align.center);

            Seq<InputDevice> devices = input.getDevices();

            Table stable = new Table();

            stable.button("<", () -> {
                int i = sectionControls.get(section, 0);
                if (i - 1 >= 0) {
                    sectionControls.put(section, i - 1);
                    section.device = devices.get(i - 1);
                    setup();
                }
            }).disabled(sectionControls.get(section, 0) - 1 < 0).size(40);

            stable.add(device).minWidth(device.getMinWidth() + 60);

            device.setText(input.getDevices().get(sectionControls.get(section, 0)).name());

            stable.button(">", () -> {
                int i = sectionControls.get(section, 0);

                if (i + 1 < devices.size) {
                    sectionControls.put(section, i + 1);
                    section.device = devices.get(i + 1);
                    setup();
                }
            }).disabled(sectionControls.get(section, 0) + 1 >= devices.size).size(40);

            //no alternate devices until further notice
            //table.add(stable).colspan(4).row();

            table.add().height(10);
            table.row();
            if (section.device.type() == InputDevice.DeviceType.controller) {
                table.table(info -> info.add("Controller Type: [lightGray]" +
                        Strings.capitalize(section.device.name())).left());
            }
            table.row();

            String lastCategory = null;
            var tstyle = Styles.defaultt;

            for (KeyBinds.KeyBind keybind : keybinds.getKeybinds()) {
                if (lastCategory != keybind.category() && keybind.category() != null) {
                    table.add(bundle.get("category." + keybind.category() + ".name", Strings.capitalize(keybind.category()))).color(Color.gray).colspan(4).pad(10).padBottom(4).row();
                    table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();
                    lastCategory = keybind.category();
                }

                if (keybind.defaultValue(section.device.type()) instanceof KeyBinds.Axis) {
                    table.add(bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), Color.white).left().padRight(40).padLeft(8);

                    table.labelWrap(() -> {
                        KeyBinds.Axis axis = keybinds.get(section, keybind);
                        return axis.key != null ? axis.key.toString() : axis.min + " [red]/[] " + axis.max;
                    }).color(Pal.accent).left().minWidth(90).fillX().padRight(20);

                    table.button("@settings.rebind", tstyle, () -> {
                        rebindAxis = true;
                        rebindMin = true;

                    }).width(130f);
                } else {
                    table.add(bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), Color.white).left().padRight(40).padLeft(8);
                    table.label(() -> keybinds.get(section, keybind).key.toString()).color(Pal.accent).left().minWidth(90).padRight(20);

                    table.button("@settings.rebind", tstyle, () -> {
                        rebindAxis = false;
                        rebindMin = false;

                    }).width(130f);
                }
                table.button("@settings.resetKey", tstyle, () -> keybinds.resetToDefault(section, keybind)).width(130f).pad(2f).padLeft(4f);
                table.row();
            }

            table.visible(() -> this.section.equals(section));

            stack.add(table);
        }

        cont.row();
        cont.add(pane).growX().colspan(sections.length);

    }
}

class DragListener extends InputListener {
    protected float lastX, lastY;
    final Table table;

    public DragListener(Table table) {
        this.table = table;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
        lastX = v.x;
        lastY = v.y;
        table.toFront();
        isDragged = false;
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
        table.setPosition(table.x + (v.x - lastX), table.y + (v.y - lastY));
        lastX = v.x;
        lastY = v.y;
        isDragged = true;
    }
}