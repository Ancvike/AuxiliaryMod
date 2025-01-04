package auxiliary.core.binding;

import arc.input.KeyCode;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;

public class BindingManager {
    public static void init() {
        if (input.keyTap(KeyCode.u)) {
            Vars.ui.hudGroup.fill(t -> {
                t.button("1111111111111", () -> {}).left().bottom();
            });
        }
    }
}