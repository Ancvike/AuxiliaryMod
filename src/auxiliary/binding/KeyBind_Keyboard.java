package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.input.InputHandler;

import static auxiliary.binding.KeyBind.keyBind_Keyboard;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;

    public void init() {
        Draw.draw(Layer.overlayUI, keyBind_Keyboard::drawTop1);

//        Events.run(EventType.Trigger.update, () -> {
//
//        });
//
//        Events.run(EventType.Trigger.update, () -> {
//            if (Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
//
//            }
//        });

        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
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

    public void drawTop1() {
        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                drawSelection(100,100,120,120,64);
            }
        });
    }
}