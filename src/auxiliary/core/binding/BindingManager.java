package auxiliary.core.binding;

import arc.input.KeyCode;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;

public class BindingManager {
    public static void init() {
        if (input.keyTap(KeyCode.u)) {
            BaseDialog dialog = new BaseDialog("Test");
            dialog.addCloseButton();
            dialog.show();
        }
    }
}