package FullResource.core.coreSetting;

import arc.struct.*;


import static FullResource.core.uiFragments.FragmentManager.sidebarSwitcherFragment;
import static FullResource.core.coreSetting.SettingHelper.*;
import static mindustry.Vars.*;

public class SharSettingUI {

    public static void init() {
        Seq<SharSetting> tapSeq = new Seq<>();
        addGraphicCheckSetting("sidebar", !mobile, tapSeq, () -> sidebarSwitcherFragment.rebuildSidebarTable());

    }
}
