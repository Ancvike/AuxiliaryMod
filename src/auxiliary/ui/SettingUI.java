package auxiliary.ui;

import arc.Core;
import mindustry.Vars;

import static auxiliary.core.Function.Setting.settingTable;
import static auxiliary.ui.Dialogs.dialog_setting;

public class SettingUI {

    public void init() {
        if (!Core.app.isDesktop()) {
            dialog_setting.show();
        } else {
            Vars.ui.settings.addCategory("AuxiliaryMod设置", settingTable);
        }
    }
}