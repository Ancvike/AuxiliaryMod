package FullResource.ui.uiFragments;

import FullResource.ui.SidebarSwitcher;
import FullResource.ui.sidebarWindows.WindowManager;

public class FragmentManager {
    public static SidebarSwitcher sidebarSwitcherFragment;

    public static void init() {
        sidebarSwitcherFragment = new SidebarSwitcher(
//                WindowManager.body
        );
    }
}