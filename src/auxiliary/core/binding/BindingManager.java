package auxiliary.core.binding;

import static arc.Core.input;
import static auxiliary.core.binding.Binding.UP;

public class BindingManager {
    public static void init() {
        input.keyTap(UP);
    }
}