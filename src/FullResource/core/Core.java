package FullResource.core;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static FullResource.ui.WindowManager.*;
import static mindustry.Vars.iconSmall;
import static mindustry.Vars.state;
import static mindustry.game.Team.sharded;

public class Core {

    public static final BaseDialog dialog_no = new BaseDialog("失败");
    public static final BaseDialog dialog_yes = new BaseDialog("");

    public Core() {
        Vars.ui.hudGroup.fill(t -> {
            t.button(Icon.upload, Styles.emptyi, this::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "full").pad(2f);
            });
            t.top();
            t.x = 300;
        });
        setDialog_no();
        setDialog_yes();
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

    public static Table getCoreItem() {
        return new Table(table -> {
            table.add(sharded.name).color(sharded.color).row();
            table.table(itemTable -> {
                CoreBlock.CoreBuild core = sharded.core();
                if (core == null || core.items == null) {
                    return;
                }
                for (int i = 0; i < Vars.content.items().size; i++) {
                    Item item = Vars.content.item(i);
                    if (!sharded.items().has(item)) continue;
                    itemTable.stack(
                            new Table(ttt -> {
                                ttt.image(item.uiIcon).size(iconSmall).tooltip(tttt -> tttt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel).margin(2f));
                                ttt.add(UI.formatAmount(core.items.get(item))).minWidth(5 * 8f).left();
                            })
                    ).padRight(3).left();
                }
            }).row();
        });
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_yes() {
        dialog_no.cont.add("确定要这样做吗?").row();
    }
}