package FullResource.ui;

import arc.scene.ui.Dialog;
import arc.scene.ui.layout.Table;
import mindustry.ui.dialogs.BaseDialog;

public class Dialogs {
    public static final BaseDialog dialog_no = new BaseDialog("失败");
    public static final BaseDialog dialog_full = new BaseDialog("确认页面");
    public static final BaseDialog dialog_restoration = new BaseDialog("确认页面");

    public static void init() {
        setDialog_no();
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_yes(Dialog dialog, Table table) {
        dialog.cont.add(table);
    }
}