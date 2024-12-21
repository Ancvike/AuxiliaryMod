package FullResource.ui;

import FullResource.ui.sidebarWindows.WindowManager;

public class FragmentManager {
    public static SidebarSwitcher sidebarSwitcherFragment;

    public static void init() {
        sidebarSwitcherFragment = new SidebarSwitcher(
                WindowManager.body
        );
    }
}