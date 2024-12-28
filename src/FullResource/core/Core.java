package FullResource.core;

import FullResource.ui.Dialogs;
import arc.scene.ui.layout.Table;

import static FullResource.ui.Dialogs.*;
import static mindustry.Vars.state;

public class Core {

    public static void init() {
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
            //先检测核心是哪个,并get资源量及上限
            dialog_yes.show();
        } else {
            dialog_no.show();
        }
    }

    public static void click_yes() {
        dialog_yes.hide();
    }
    public static void click_no() {
        dialog_yes.hide();
    }
}