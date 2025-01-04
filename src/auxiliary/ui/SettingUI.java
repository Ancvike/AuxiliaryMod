package auxiliary.ui;

import arc.Core;

import static auxiliary.dialogs.Dialogs.dialog_setting;

public class SettingUI {

    public static void init() {
        if (Core.app.isDesktop()) {
            dialog_setting.show();
        }
    }
}