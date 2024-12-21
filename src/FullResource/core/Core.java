package FullResource.core;

import FullResource.ui.UI;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.state;

public class Core {
    public BaseDialog baseDialog_no = new BaseDialog("失败");
    public BaseDialog baseDialog_yes = new BaseDialog("成功");
    Table table = new Table();

    public Core() {
        baseDialog_no_show();
        baseDialog_yes_show();
        UI ui = new UI();
        ui.imageButton.clicked(this::onClick);
    }

    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
            //先检测核心是哪个,并get资源量及上限
            Vars.ui.hudGroup.fill(t -> t.add(table));
            baseDialog_yes.show();
        } else {
            baseDialog_no.show();
        }
    }

    public void baseDialog_no_show() {
        baseDialog_no.cont.add("区块未占领,无法使用该功能").row();
        baseDialog_no.cont.image(arc.Core.atlas.find("full-resource-frog")).pad(20f).row();
        baseDialog_no.cont.button("了解", baseDialog_no::hide).size(100f, 50f);
    }

    public void baseDialog_yes_show() {
        baseDialog_yes.cont.button("了解", baseDialog_yes::hide).size(100f, 50f);
    }
}
