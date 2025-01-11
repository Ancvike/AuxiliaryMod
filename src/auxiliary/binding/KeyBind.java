package auxiliary.binding;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;

public class KeyBind {
    private static boolean dialogShown = false;

    public static void init() {

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (input.keyDown(CONVEYOR_CHANGE.nowKeyCode) && !dialogShown) {
                BaseDialog dialog = new BaseDialog("传送带升级");
                dialog.addCloseButton();
                dialog.show();
                dialogShown = true;
            } else if (input.keyRelease(CONVEYOR_CHANGE.nowKeyCode)) {
                dialogShown = false;
            }
        });

    }
}