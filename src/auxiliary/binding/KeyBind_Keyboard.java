package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.input.Placement;

import static auxiliary.binding.MyKeyBind.RECOVERY_BUDDING;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;
    int startX, startY;
    int endX, endY;
    boolean isDraw = false;

    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (!Core.scene.hasKeyboard() && startX != 0 && startY != 0 && endX != 0 && endY != 0) {
                if (Core.input.keyRelease(RECOVERY_BUDDING.nowKeyCode)) {
                    rebuildArea(startX, startY, endX, endY);
                    startX = 0;
                    startY = 0;
                    isDraw = true;
//                    Vars.ui.hudfrag.showToast("所选建筑已修复");
                }
            }

            if (Core.input.keyTap(RECOVERY_BUDDING.nowKeyCode) && !Core.scene.hasKeyboard()) {
                startX = World.toTile(Core.input.mouseWorld().x);
                startY = World.toTile(Core.input.mouseWorld().y);
            }
        });

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (Core.input.keyDown(RECOVERY_BUDDING.nowKeyCode)) {

                endX = World.toTile(Core.input.mouseWorld().x);
                endY = World.toTile(Core.input.mouseWorld().y);
                drawRebuildSelection(startX, startY, endX, endY);
                if (isDraw) {
                    Vars.ui.hudfrag.showToast("所选建筑已修复");
                    isDraw = false;
                }
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
}