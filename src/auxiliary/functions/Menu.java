package auxiliary.functions;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Label;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.graphics.Pal;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.state;

public class Menu {
    ImageButton button = new ImageButton(Icon.menu);
    public static BaseDialog dialog = new BaseDialog("功能面板");
    static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

    public Menu() {
        Events.run(EventType.Trigger.update, () -> button.visible = !(!Vars.ui.hudfrag.shown || Vars.ui.minimapfrag.shown()));
        setDialog(dialog);
        button.clicked(this::onClick);
        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            t.add(button);
            t.right();
        });
        button.addListener(new DragListener(button));
    }

    public void setDialog(BaseDialog dialog) {
        functions.addAll(new SpeedChange(), new Light(), new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove(), new Launch());

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
                            }).growX().height(50f).margin(0).padLeft(10).center();
                        }).growX().height(50f).margin(0).pad(0).row();
                    }
                }
            }).size(width / 2f, height);
        }).size(width, height);

        dialog.addCloseButton();
    }

    public void onClick() {
        if (!isDragged) dialog.show();
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