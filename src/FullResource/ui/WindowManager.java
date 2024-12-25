package FullResource.ui;

import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class WindowManager {
    public static CoreWindow coreWindow = new CoreWindow();
    public static final BaseDialog dialog_no = new BaseDialog("失败");

    public static void init() {
        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";

            coreWindow.build();
            t.add(coreWindow).height(coreWindow.getHeight()).width(coreWindow.getWidth());
        });

        setDialog_no();
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.cont.button("确认", dialog_no::hide).size(100f, 50f);
    }
}