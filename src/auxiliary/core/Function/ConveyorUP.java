package auxiliary.core.Function;

import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.core.binding.MyKeyBind.UP;

public class ConveyorUP {

    public static void init() {

        if (input.keyTap(UP.nowKeyCode)) {
            BaseDialog dialog = new BaseDialog("Conveyor UP");
            dialog.addCloseButton();
            dialog.show();
        }
    }
}
