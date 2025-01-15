package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;

    public void init() {
        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                Draw.z(Layer.max);

                Lines.stroke(1f);
                Draw.color(Pal.accent);

                drawSelection(100, 100, 120, 120, 64);

                Draw.reset();
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