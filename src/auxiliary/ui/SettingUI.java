package auxiliary.ui;

import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class SettingUI {
    static Table table = new Table();
    public static void init() {
        Vars.ui.settings.addCategory("AuxiliaryMod设置", table -> {});
    }
}
