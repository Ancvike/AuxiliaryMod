package auxiliary.ui;

import arc.Input;
import arc.input.KeyCode;
import mindustry.Vars;

public class SettingUI {
    public static Input input;
    public static void init() {
        Vars.ui.settings.addCategory("AuxiliaryMod设置", table -> input.keyTap(KeyCode.u));
    }
}
