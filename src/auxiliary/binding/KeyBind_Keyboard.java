package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.input.InputHandler;

import static auxiliary.binding.MyKeyBind.RECOVERY_BUDDING;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;
    int schemX = -1, schemY = -1;

    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (!Core.scene.hasKeyboard() && Core.input.keyDown(RECOVERY_BUDDING.nowKeyCode)) {
                drawRebuildSelection(schemX, schemY, tileX(Core.input.mouseX()), tileY(Core.input.mouseY()));
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (!Core.scene.hasKeyboard() && schemX != -1 && schemY != -1) {
                if (Core.input.keyRelease(RECOVERY_BUDDING.nowKeyCode)) {
                    rebuildArea(schemX, schemY, World.toTile(Core.input.mouseWorld().x), World.toTile(Core.input.mouseWorld().y));
                    schemX = -1;
                    schemY = -1;
                }
            }

            if (Core.input.keyTap(RECOVERY_BUDDING.nowKeyCode) && !Core.scene.hasKeyboard()) {
                schemX = World.toTile(Core.input.mouseWorld().x);
                schemY = World.toTile(Core.input.mouseWorld().y);
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode && !is) {
                Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                for (Unit unit : selectedUnits) {
                    unit.health = unit.maxHealth;
                }
                Vars.ui.hudfrag.showToast("所选单位已修复");
                is = true;
            } else if (Core.input.keyRelease(MyKeyBind.RECOVERY_UNIT.nowKeyCode)) {
                is = false;
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