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

    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (!Core.scene.hasKeyboard() && startX != 0 && startY != 0 && endX != 0 && endY != 0) {
                if (Core.input.keyRelease(RECOVERY_BUDDING.nowKeyCode)) {
                    rebuildArea(startX, startY, endX, endY);
                    startX = 0;
                    startY = 0;
//                    Vars.ui.hudfrag.showToast("所选建筑已修复");
                }
            }

            if (Core.input.keyTap(RECOVERY_BUDDING.nowKeyCode) && !Core.scene.hasKeyboard()) {
                startX = World.toTile(Core.input.mouseWorld().x);
                startY = World.toTile(Core.input.mouseWorld().y);
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(RECOVERY_BUDDING.nowKeyCode)) {
//                Lines.stroke(1f);
                endX = World.toTile(Core.input.mouseWorld().x);
                endY = World.toTile(Core.input.mouseWorld().y);
                Placement.NormalizeDrawResult result = Placement.normalizeDrawArea(Blocks.air, startX, startY, endX, endY, false, 0, 1f);

                Lines.stroke(2f);
                Vars.ui.hudfrag.showToast("所选建筑已修复");
                Draw.color(Pal.darkerGray);
                Lines.rect(result.x, result.y - 1, result.x2 - result.x, result.y2 - result.y);
                Draw.color(Pal.gray);
                Lines.rect(result.x, result.y, result.x2 - result.x, result.y2 - result.y);
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