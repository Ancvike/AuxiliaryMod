package FullResource.ui;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static mindustry.Vars.iconSmall;
import static mindustry.game.Team.sharded;

public class Dialogs {
    public static final BaseDialog dialog_no = new BaseDialog("失败");
    public static final BaseDialog dialog_yes = new BaseDialog("");
    private static Table itemTable;

    public static void init() {
        setDialog_no();
    }

    public static void setDialog_no() {
        dialog_no.cont.add("区块未占领,无法使用该功能").row();
        dialog_no.addCloseButton();
    }

    public static void setDialog_yes() {
        dialog_no.cont.add("确定要这样做吗?").row();
        dialog_yes.cont.add(itemTable).row();
        dialog_yes.addCloseButton();
    }

    public static void getCoreItems() {
        itemTable = new Table(table -> {
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
}