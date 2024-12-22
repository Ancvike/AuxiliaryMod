package FullResource.core;

import FullResource.ui.CoreWindow;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.state;

public class Core {
    public static BaseDialog baseDialog_no = new BaseDialog("失败");
    public static BaseDialog baseDialog_yes = new BaseDialog("成功");
    public static Table body;
    public static CoreWindow coreWindow = new CoreWindow();

    public static void init() {
        baseDialog_no_show();
        baseDialog_yes_show();

        Vars.ui.hudGroup.fill(t -> {
            t.button(coreWindow.icon, Styles.emptyi, Core::onClick);
            t.top();
            t.x = 300;
        });
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {//区块是否占领
            //先检测核心是哪个,并get资源量及上限
            coreWindow.parent.setLayoutEnabled(false);
            coreWindow.toggle();
            coreWindow.setLayoutEnabled(true);
        } else {
            baseDialog_no.show();
        }
    }

    public static void baseDialog_no_show() {
        baseDialog_no.cont.add("区块未占领,无法使用该功能").row();
        baseDialog_no.cont.image(arc.Core.atlas.find("full-resource-frog")).pad(20f).row();
        baseDialog_no.cont.button("了解", baseDialog_no::hide).size(100f, 50f);
    }

    public static void baseDialog_yes_show() {
        baseDialog_yes.cont.button("了解", baseDialog_yes::hide).size(100f, 50f);
    }


    public static void update() {
        body = new Table(t -> {
            t.name = "Window Buttons";
            t.left();
            t.button(coreWindow.icon, Styles.emptyi, () -> {

            }).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "check").pad(2f);
            });
        }).left();
    }
}
