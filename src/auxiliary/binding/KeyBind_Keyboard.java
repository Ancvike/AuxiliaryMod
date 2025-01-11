package auxiliary.binding;

import arc.Core;
import arc.Events;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;

public class KeyBind_Keyboard {
    public static boolean is = false;

    public static void init() {
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (Core.input.keyDown(CONVEYOR_CHANGE.nowKeyCode) && !is) {
                BaseDialog dialog = new BaseDialog("传送带升级");
                dialog.addCloseButton();
                dialog.show();
                is = true;
            } else if (Core.input.keyRelease(CONVEYOR_CHANGE.nowKeyCode)) {
                is = false;
            }
        });
    }
}