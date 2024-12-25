package FullResource.core;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.state;
import static FullResource.ui.WindowManager.coreWindow;

public class Core {
    public static final BaseDialog dialog_no = new BaseDialog("失败");

    public Core() {
        setDialog_no();

        Vars.ui.hudGroup.fill(t -> {
            t.button(Icon.upload, Styles.emptyi, this::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "full").pad(2f);
            });
            t.top();
            t.x = 300;
        });
    }

    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
        //先检测核心是哪个,并get资源量及上限
            success();
        } else {
            dialog_no.show();
        }
    }

    public void success() {
        coreWindow.parent.setLayoutEnabled(false);
        coreWindow.toggle();
        coreWindow.setLayoutEnabled(true);
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.cont.button("确认", dialog_no::hide).size(100f, 50f);
    }
}