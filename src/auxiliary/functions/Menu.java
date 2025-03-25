package auxiliary.functions;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.*;
import arc.scene.ui.layout.Stack;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.binding.KeyBind.isOpen;
import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.mobile;

public class Menu {
    Table table = new Table();
    ImageButton button = new ImageButton(Icon.menu);
    public static BaseDialog dialog = new BaseDialog("功能面板");
    public static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

    public Menu() {
        Events.run(EventType.Trigger.update, () -> table.visible = !(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()));

        setDialog(dialog);

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

        Stack stack = new Stack();
        ScrollPane pane = new ScrollPane(stack);
        pane.setFadeScrollBars(false);

        dialog.cont.row();
        dialog.cont.add(pane).growX().colspan(functions.size);

        dialog.cont.add("通用").color(Color.gray).colspan(4).pad(10).padBottom(4).row();
        dialog.cont.image().color(Color.gray).fillX().height(3).pad(6).colspan(4).padTop(0).padBottom(10).row();

        Table table = new Table();
        for (Function function : functions) {
            table.add(function.getName());
            table.row();
        }

        stack.add(table);
//        dialog.cont.add(stack);

//        int width = Core.graphics.getWidth() / 2;
//        int height = Core.graphics.getHeight() - 64;
//
//        dialog.cont.table(main -> {
//            main.defaults().growX().fillX().margin(0).pad(0);
//
//            main.table(list -> {
//                for (Function function : functions) {
//                    list.add(function.getName()).height(50).row();
//                }
//            }).size(width / 2f, height);
//
//            main.table(actions -> {
//                for (Function function : functions) {
//                    if (function.getButtonID() == 0) {
//                        actions.button("使用", function::onClick).size(100, 50).row();
//                    } else if (function.getButtonID() == 1) {
//                        actions.table(sliderTable -> {
//                            sliderTable.defaults().growX().fillX();
//
//                            sliderTable.add("开").width(20f).color(Color.green);
//
//                            sliderTable.add(function.slider).growX().height(50f).padLeft(20f).padRight(20).margin(0);
//
//                            sliderTable.add("关").width(20f).color(Color.red);
//                        }).growX().height(50f).row();
//                    } else if (function.getButtonID() == 2) {
//                        actions.table(speedTable -> {
//                            speedTable.defaults().growX().fillX().margin(0).pad(0);
//
//                            Label speedLabel = new Label("1x");
//                            speedLabel.setColor(Pal.accent);
//                            speedTable.add(speedLabel).width(20f).left().margin(0).pad(0);
//
//                            float[] speedValues = {1f, 2f, 5f, 10f, 20f};
//                            speedTable.slider(0, speedValues.length - 1, 1, 0, value -> {
//                                float selectedSpeed = speedValues[(int) value];
//                                Time.setDeltaProvider(() -> Math.min(Core.graphics.getDeltaTime() * 60.0f * selectedSpeed, 3.0f));
//                                speedLabel.setText((int) selectedSpeed + "x");
//                            }).growX().height(50f).margin(0).padLeft(10);
//                        }).growX().height(50f).margin(0).pad(0).row();
//                    }
//                }
//            }).size(width / 2f, height);
//        }).size(width, height);

        dialog.addCloseButton();
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