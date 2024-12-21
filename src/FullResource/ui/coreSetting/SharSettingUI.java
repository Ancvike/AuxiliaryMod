package FullResource.ui.coreSetting;

import FullResource.ui.SidebarSwitcher;
import FullResource.ui.sidebarWindows.WindowManager;
import arc.struct.Seq;

import static mindustry.Vars.mobile;

public class SharSettingUI {
    public static SidebarSwitcher sidebarSwitcherFragment;

    public static void init() {
        Seq<SharSetting> tapSeq = new Seq<>();
        addGraphicCheckSetting("sidebar", !mobile, tapSeq, () -> sidebarSwitcherFragment.rebuildSidebarTable());
        sidebarSwitcherFragment = new SidebarSwitcher(
                WindowManager.body
        );
    }
    public static void addGraphicCheckSetting(String key, boolean def, Seq<SharSetting> list, Runnable onSetted) {
        list.add(new SharSetting(key, def) {});
    }
}
