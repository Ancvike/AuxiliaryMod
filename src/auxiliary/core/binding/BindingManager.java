package auxiliary.core.binding;

import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.core.binding.Binding.UP;

public class BindingManager {
    public static void init() {
        if (input.keyTap(UP)) {
            BaseDialog dialog = new BaseDialog("test");
            dialog.addCloseButton();
            dialog.show();
        }
    }
}