package FullResource.ui.coreSetting;

import arc.Core;
import mindustry.ui.dialogs.SettingsMenuDialog;

abstract class SharSetting extends SettingsMenuDialog.SettingsTable.Setting {

    public SharSetting(String name) {
        super(name);
    }

    public SharSetting(String name, Object def) {
        this(name);
        Core.settings.defaults(name, def);
    }

    public void add(SettingsMenuDialog.SettingsTable table) {
    }
}
