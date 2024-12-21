package FullResource.ui.coreSetting;

import FullResource.ui.SidebarSwitcher;
import FullResource.ui.sidebarWindows.WindowManager;
import arc.struct.Seq;

import static FullResource.ui.uiFragments.FragmentManager.sidebarSwitcherFragment;
import static mindustry.Vars.mobile;

public class SharSettingUI {

    public static void init() {
        sidebarSwitcherFragment = new SidebarSwitcher(
                WindowManager.body
        );
        Seq<SharSetting> tapSeq = new Seq<>();
        addGraphicCheckSetting("sidebar", !mobile, tapSeq, () -> sidebarSwitcherFragment.rebuildSidebarTable());

    }

    public static void addGraphicCheckSetting(String key, boolean def, Seq<SharSetting> list, Runnable onSetted) {
        list.add(new SharSetting(key, def) {
        });
    }
}
