package auxiliary.functions;

import arc.Core;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.ImageButton;
import arc.scene.ui.Slider;
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
        if (mobile && !Core.settings.getBool("landscape")) {
            setDialog_mobile(dialog);
        } else {
            setDialog(dialog);
        }
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
        int width = Core.graphics.getWidth() / 4;
        int height = Core.graphics.getHeight() - 64;
        dialog.cont.table().size(width, height);
        dialog.cont.table(t -> {
            for (Function function : functions) {
                t.add(function.getName()).height(50).row();
            }
        }).size(width, height);
        dialog.cont.table(t -> {
            for (Function function : functions) {
                if (function.getButtonID() == 0) t.button("使用", function::onClick).size(100, 50).row();
                if (function.getButtonID() == 1) {
                    t.add("开");
                    t.slider(0, 50, 50, 50, isOpen -> {
                        if (isOpen == 0) state.rules.fog = true;
                        else if (isOpen == 50) state.rules.fog = false;
                    });
                    t.add("关");
                    t.row();
                }
            }
        }).size(width, height);
        dialog.cont.table().size(width, height);
        dialog.addCloseButton();
    }

    public void setDialog_mobile(BaseDialog dialog) {
        functions.addAll(new WarfareFog(), new FullResource(), new BuildingRestoration(), new UnitsRestoration(), new DerelictRemove());
        int width = Core.graphics.getWidth() / 2;
        int height = Core.graphics.getHeight() - 64;
        dialog.cont.table(t -> {
            for (Function function : functions) {
                t.add(function.getName()).height(50).row();
            }
        }).size(width, height);
        dialog.cont.table(t -> {
            for (Function function : functions) {
                if (function.getButtonID() == 0) t.button("使用", function::onClick).size(100, 50).row();
                if (function.getButtonID() == 1) {
                    t.add("开");
                    Slider slider = new Slider(0, 50, 50, false);
                    slider.moved(isOpen -> {
                        if (isOpen == 0) state.rules.fog = true;
                        else if (isOpen == 50) state.rules.fog = false;
                    });
                    t.add(slider);
                    t.add("关");
                    t.row();
                }
            }
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