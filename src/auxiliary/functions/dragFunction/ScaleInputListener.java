package auxiliary.functions.dragFunction;

import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.layout.Table;
import arc.util.Tmp;

public class ScaleInputListener extends InputListener {
    protected float lastX, lastY;
    final Table table;
    
    public ScaleInputListener(Table table) {
        this.table = table;
    }
    
    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
        lastX = v.x;
        lastY = v.y;
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
        float w = v.x - lastX;
        float h = v.y - lastY;

        if (table.getWidth() < 280) table.setWidth(280);
        if (table.getWidth() + w < 280 || table.getWidth() + w > Float.MAX_VALUE)
            w = 0;
        if (table.getHeight() - h < 60 || table.getHeight() - h > Float.MAX_VALUE)
            h = 0;
        table.sizeBy(w, -h);
        table.moveBy(0, h);
        lastX = v.x;
        lastY = v.y;
    }
}
