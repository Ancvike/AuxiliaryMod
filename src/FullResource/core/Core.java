package FullResource.core;

import FullResource.ui.Dialogs;
import arc.scene.ui.layout.Table;

import static FullResource.ui.Dialogs.*;
import static mindustry.Vars.state;

public class Core {
    public static Table itemTable = new Table();

    public static void init() {
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
            //先检测核心是哪个,并get资源量及上限
            itemTable = Dialogs.getCoreItems();
            Dialogs.setDialog_yes(itemTable);
            dialog_yes.show();
        } else {
            dialog_no.show();
        }
    }

    public static void click_yes() {
        resetDialog(itemTable);
        dialog_yes.hide();
        dialog_no.show();
    }
    public static void click_no() {
        resetDialog(itemTable);
        dialog_yes.hide();
        dialog_no.show();
    }
}