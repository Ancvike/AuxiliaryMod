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
import arc.struct.OrderedMap;
import arc.struct.Seq;
import arc.util.Align;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

import static arc.Core.*;
import static arc.Core.bundle;
import static auxiliary.binding.KeyBind.isOpen;
import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.mobile;

public class Menu {
    Table table = new Table();
    ImageButton button = new ImageButton(Icon.menu);
    static MyDialog dialog = new MyDialog(
            //"功能面板"
    );

    Dialog dialog2 = new Dialog("功能面板") {{
        setFillParent(true);
        title.setAlignment(Align.center);
        titleTable.row();
        titleTable.add(new Image()).growX().height(3f).pad(4f).get().setColor(Pal.accent);

        buttons.button("返回", Icon.left, this::hide).size(210f, 64f);

        keyDown(key -> {
            if (key == KeyCode.escape || key == KeyCode.back) hide();
        });

        cont.clear();

        Stack stack = new Stack();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);

        Table table = new Table();

        table.add("aaaaaa").color(Color.gray).colspan(4).pad(10).padBottom(4).row();
        table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

        table.add(new SpeedChange().getName(), Color.white).left().padRight(40).padLeft(8);
//        table.label(() -> "1111").color(Pal.accent).left().minWidth(90).padRight(20);
//
//        table.button("绑定", Styles.defaultt, () -> {
//        }).width(130f);

//        table.button("重置", Styles.defaultt, () -> {
//
//        }).width(130f).pad(2f).padLeft(4f);
        table.add(new SpeedChange().function()).width(150f).pad(2f).padLeft(4f);
        table.row();

        table.add("bbbbbb").color(Color.gray).colspan(4).pad(10).padBottom(4).row();
        table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

        stack.add(table);

        cont.row();
        cont.add(pane).growX().colspan(2);
    }};
    public static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

    public Menu() {
        Events.run(EventType.Trigger.update, () -> table.visible = !(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()));

        table.add(button).row();
        if (mobile) {
            ImageButton androidButton = getImageButton();
            table.add(androidButton);
        }

        button.clicked(() -> {
            if (!isDragged) dialog2.show();
        });

        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            t.add(table).row();
            t.right();
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

    public void setDialog(Dialog dialog) {
        functions.addAll(new SpeedChange(), new SunLight(), new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove(), new Launch());

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

class MyDialog extends Dialog {
    protected KeyBinds.Section section;
    protected KeyBinds.KeyBind rebindKey = null;
    protected boolean rebindAxis = false;
    protected boolean rebindMin = true;
    protected KeyCode minKey = null;
    protected Dialog rebindDialog;
    protected float scroll;

    public MyDialog() {
        super("功能面板");
        setup();
        addCloseButton();
        setFillParent(true);
        title.setAlignment(Align.center);
        titleTable.row();
        titleTable.add(new Image()).growX().height(3f).pad(4f).get().setColor(Pal.accent);
    }

    @Override
    public void addCloseButton() {
        buttons.button("返回", Icon.left, this::hide).size(210f, 64f);

        keyDown(key -> {
            if (key == KeyCode.escape || key == KeyCode.back) hide();
        });
    }

    private void setup() {
        cont.clear();

        KeyBinds.Section[] sections = Core.keybinds.getSections();

        Stack stack = new Stack();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);
        pane.update(() -> scroll = pane.getScrollY());
        this.section = sections[0];

        for (KeyBinds.Section section : sections) {

            Table table = new Table();

            String lastCategory = null;
            var tstyle = Styles.defaultt;

            for (KeyBinds.KeyBind keybind : keybinds.getKeybinds()) {
                if (lastCategory != keybind.category() && keybind.category() != null) {
                    table.add(keybind.category()).color(Color.gray).colspan(4).pad(10).padBottom(4).row();
                    table.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();
                    lastCategory = keybind.category();
                }

                if (!(keybind.defaultValue(section.device.type()) instanceof KeyBinds.Axis)) {
                    table.add(keybind.name(), Color.white).left().padRight(40).padLeft(8);
                    table.label(() -> keybinds.get(section, keybind).key.toString()).color(Pal.accent).left().minWidth(90).padRight(20);

                    table.button("绑定", tstyle, () -> {
                        rebindAxis = false;
                        rebindMin = false;
                        openDialog(section, keybind);
                    }).width(130f);

                    table.button("重置", tstyle, () -> keybinds.resetToDefault(section, keybind)).width(130f).pad(2f).padLeft(4f);
                    table.row();
                }
            }

            table.visible(() -> this.section.equals(section));

            stack.add(table);
        }

        cont.row();
        cont.add(pane).growX().colspan(sections.length);
    }

    void rebind(KeyBinds.Section section, KeyBinds.KeyBind bind, KeyCode newKey) {
        if (rebindKey == null) return;
        rebindDialog.hide();
        boolean isAxis = bind.defaultValue(section.device.type()) instanceof KeyBinds.Axis;

        if (isAxis) {
            if (newKey.axis || !rebindMin) {
                section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, newKey.axis ? new KeyBinds.Axis(newKey) : new KeyBinds.Axis(minKey, newKey));
            }
        } else {
            section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, new KeyBinds.Axis(newKey));
        }

        if (rebindAxis && isAxis && rebindMin && !newKey.axis) {
            rebindMin = false;
            minKey = newKey;
            openDialog(section, rebindKey);
        } else {
            rebindKey = null;
            rebindAxis = false;
        }
    }

    private void openDialog(KeyBinds.Section section, KeyBinds.KeyBind name) {
        rebindDialog = new Dialog(rebindAxis ? bundle.get("keybind.press.axis") : bundle.get("keybind.press"));

        rebindKey = name;

        rebindDialog.titleTable.getCells().first().pad(4);

        if (section.device.type() == InputDevice.DeviceType.keyboard) {

            rebindDialog.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    if (Core.app.isAndroid()) return false;
                    rebind(section, name, button);
                    return false;
                }

                @Override
                public boolean keyDown(InputEvent event, KeyCode keycode) {
                    rebindDialog.hide();
                    rebind(section, name, keycode);
                    return false;
                }

                @Override
                public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                    if (!rebindAxis) return false;
                    rebindDialog.hide();
                    rebind(section, name, KeyCode.scroll);
                    return false;
                }
            });
        }

        rebindDialog.show();
        Time.runTask(1f, () -> getScene().setScrollFocus(rebindDialog));
    }
}