package auxiliary.ui;

import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

public class Dialogs {
    public static final BaseDialog dialog_no = new BaseDialog("失败");
    public static final BaseDialog dialog_full = new BaseDialog("确认页面");
    public static final BaseDialog dialog_restoration = new BaseDialog("确认页面");
    public static final BaseDialog dialog_setting = new BaseDialog("确认页面");

    public static void init() {
        setDialog_no();
        setDialog_setting();
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_setting() {
        dialog_no.cont.add("当前Mod设置只有电脑端的更改键盘键位设置,其他端无需该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_yes(Dialog dialog, Table table) {
        dialog.cont.add(table);
    }
}