package FullResource.ui.coreSetting;

import arc.struct.*;


import static FullResource.ui.uiFragments.FragmentManager.sidebarSwitcherFragment;
import static FullResource.ui.coreSetting.SettingHelper.*;
import static mindustry.Vars.*;

public class SharSettingUI {

    public static void init() {
        Seq<SharSetting> tapSeq = new Seq<>();
        addGraphicCheckSetting("sidebar", !mobile, tapSeq, () -> sidebarSwitcherFragment.rebuildSidebarTable());

    }
}
