package auxiliary.binding;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;

public class KeyBind {
    public static void init() {
        if (Vars.state.isGame()) {
            Events.run(EventType.Trigger.uiDrawEnd, () -> {
                if (input.keyDown(CONVEYOR_CHANGE.nowKeyCode)) {
                    BaseDialog dialog = new BaseDialog("传送带升级");
                    dialog.addCloseButton();
                    dialog.show();
                }
            });
        }
    }
}
