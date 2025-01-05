package auxiliary.ui;

import arc.Core;
import arc.func.Cons;
import mindustry.Vars;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static auxiliary.dialogs.Dialogs.dialog_setting;

public class SettingUI {
    static Cons<SettingsMenuDialog.SettingsTable> settingTable = table -> {
        if (Core.app.isDesktop()) {

        } else {
            dialog_setting.show();
        }
    };

    public static void init() {
        Vars.ui.settings.addCategory("AuxiliaryMod设置", settingTable);
    }
}