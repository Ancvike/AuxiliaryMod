package auxiliary.core.Function;

import arc.Core;
import mindustry.Vars;
import mindustry.input.Binding;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.assets;

public class ConveyorUP {

    public static void init() {
        //????????????????????????????????
        if (assets.update(500)) {
            if (!Core.app.isDesktop()) return;
            if (Vars.state.isGame()) {
                if (!Core.scene.hasKeyboard()) {
                    if (Core.input.keyDown(Binding.schematic_select)) {
                        BaseDialog dialog = new BaseDialog("");
                        dialog.addCloseButton();
                        dialog.show();
                    }
                }
            }
        }
    }
}