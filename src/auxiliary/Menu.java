package auxiliary;

import arc.Core;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.Menu.isDragged;

public class Menu {
    ImageButton button = new ImageButton(Icon.menu);
    BaseDialog dialog = new BaseDialog("功能面板");
    static boolean isDragged = false;

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
        int width = Core.graphics.getWidth() / 4;
        int height = Core.graphics.getHeight() - 64;
        dialog.cont.table().size(width, height);
        dialog.cont.table(t -> {
            t.add("aaa").top().row();
            t.add("bbb").top().row();
        }).size(width, height);
        dialog.cont.table(t -> {
            t.add("aaa").top().row();
            t.add("bbb").top().row();
        }).size(width, height);
        dialog.cont.table().size(width, height);
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