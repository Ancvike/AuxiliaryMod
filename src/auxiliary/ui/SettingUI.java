package auxiliary.ui;

import arc.input.KeyCode;
import mindustry.Vars;

import static arc.Core.input;

public class SettingUI {
    public static void init() {
        Vars.ui.settings.addCategory("AuxiliaryMod设置", table -> input.keyTap(KeyCode.u));
    }
}
