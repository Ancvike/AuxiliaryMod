package FullResource.ui.uiFragments;

import FullResource.ui.SidebarSwitcher;
import FullResource.ui.sidebarWindows.WindowManager;
import mindustry.ui.dialogs.BaseDialog;

public class FragmentManager {
    public static SidebarSwitcher sidebarSwitcherFragment;

    public static void init() {
//        sidebarSwitcherFragment = new SidebarSwitcher(
//                WindowManager.body
//        );
        BaseDialog baseDialog = new BaseDialog("按钮测试页");
        baseDialog.addCloseButton();
        baseDialog.show();
    }
}