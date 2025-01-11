package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.math.geom.Vec2;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.input.InputHandler;

import static arc.Core.input;
import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;
import static mindustry.Vars.state;

public class KeyBind_Keyboard extends InputHandler {
    public int schemX = -1, schemY = -1;

    public void init() {
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (state.isGame()) {
                if (Core.input.keyDown(CONVEYOR_CHANGE.nowKeyCode)) {
                    schemX = World.toTile(Core.input.mouseWorld().x);
                    schemY = World.toTile(Core.input.mouseWorld().y);
                    int cursorX = tileX(Core.input.mouseX());
                    int cursorY = tileY(Core.input.mouseY());
                    drawRebuildSelection(schemX, schemY, cursorX, cursorY);
                } else if (input.keyRelease(CONVEYOR_CHANGE.nowKeyCode)) {
                    int rawCursorX = World.toTile(Core.input.mouseWorld().x);
                    int rawCursorY = World.toTile(Core.input.mouseWorld().y);
                    rebuildArea(schemX, schemY, rawCursorX, rawCursorY);
                    schemX = -1;
                    schemY = -1;
                }
            }
        });
    }

    int tileX(float cursorX) {
        Vec2 vec = Core.input.mouseWorld(cursorX, 0);
        if (selectedBlock()) {
            vec.sub(block.offset, block.offset);
        }
        return World.toTile(vec.x);
    }

    int tileY(float cursorY) {
        Vec2 vec = Core.input.mouseWorld(0, cursorY);
        if (selectedBlock()) {
            vec.sub(block.offset, block.offset);
        }
        return World.toTile(vec.y);
    }
}