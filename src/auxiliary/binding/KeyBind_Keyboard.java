package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.input.InputHandler;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;

    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            drawRebuildSelection(0, 0, 100, 100);
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