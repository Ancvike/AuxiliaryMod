package auxiliary.ui;

import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class SettingUI {
    public static void init() {
        Vars.ui.settings.button("AuxiliaryMod设置", Icon.settings, Styles.flatt, 8*4f, () -> {});
    }
}
