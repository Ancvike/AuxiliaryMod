package FullResource.ui;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class WindowManager {
    public static CoreWindow coreWindow = new CoreWindow();
    public static final BaseDialog dialog_no = new BaseDialog("失败");
    public static final BaseDialog dialog_yes = new BaseDialog("");

    public static void init() {
        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";

            coreWindow.build();
            t.add(coreWindow).height(coreWindow.getHeight()).width(coreWindow.getWidth());
        });

        setDialog_no();
        setDialog_yes();
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_yes() {
        dialog_no.cont.add("确定要这样做吗?").row();

        dialog_no.button("确定", WindowManager::clearDialog_yes);
    }

    public static void clearDialog_yes() {
        dialog_yes.clear();
    }
}