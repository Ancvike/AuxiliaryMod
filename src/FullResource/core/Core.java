package FullResource.core;

import FullResource.ui.Dialogs;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.world.blocks.storage.CoreBlock;

import static FullResource.ui.Dialogs.dialog_no;
import static FullResource.ui.Dialogs.dialog_yes;
import static mindustry.Vars.state;

public class Core {

    public static void init() {
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
            //先检测核心是哪个,并get资源量及上限
            Dialogs.getCoreItems();
            Dialogs.setDialog_yes();
            dialog_yes.show();
        } else {
            dialog_no.show();
        }
    }
}