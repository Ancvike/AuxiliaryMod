package auxiliary.functions;

import arc.Core;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.functions.Menu.isDragged;
import static mindustry.Vars.mobile;
import static mindustry.Vars.state;

public class Menu {
    ImageButton button = new ImageButton(Icon.menu);
    public static BaseDialog dialog = new BaseDialog("功能面板");
    static boolean isDragged = false;
    Seq<Function> functions = new Seq<>();

    public Menu() {
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
        functions.addAll(new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove());

        int width = mobile && !Core.settings.getBool("landscape") ? Core.graphics.getWidth() / 2 : Core.graphics.getWidth() / 4;
        int height = Core.graphics.getHeight() - 64;

        dialog.cont.clear(); // 清空原有内容
        dialog.cont.table(t -> {
            t.defaults().growX().fillX().margin(0).pad(0);

            t.table(list -> {
                for (Function function : functions) {
                    list.add(function.getName()).height(50).row();
                }
            }).width(width / 2f).height(height);

            t.table(actions -> {
                for (Function function : functions) {
                    if (function.getButtonID() == 0) {
                        actions.button("使用", function::onClick).size(100, 50).row();
                    } else if (function.getButtonID() == 1) {
                        actions.table(sliderTable -> {
                            sliderTable.defaults().growX().fillX();

                            sliderTable.add("开").width(20f).left();

                            sliderTable.slider(0, 50, 50, state.rules.fog ? 0 : 50, moved -> {
                                if (moved == 0) state.rules.fog = true;
                                else if (moved == 50) state.rules.fog = false;
                            }).growX().height(50f);

                            sliderTable.add("关").width(20f).right();
                        }).growX().height(50f);
                        actions.row();
                    }
                }
            }).width(width / 2f).height(height);
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