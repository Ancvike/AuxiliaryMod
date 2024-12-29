package FullResource.core;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.world.blocks.storage.CoreBlock;

import static FullResource.ui.Dialogs.*;
import static mindustry.Vars.state;
import static mindustry.game.Team.sharded;

public class Core {
    private static Table itemTable;
    public static void init() {
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            itemTable = setTable();
            setDialog_yes(itemTable);
            dialog_yes.show();
        } else {
            dialog_no.show();
        }
    }

    public static Table setTable() {
        return new Table(t -> {
            t.add("资源列表").row();
            CoreBlock.CoreBuild core = sharded.core();
            if (core == null || core.items == null) {
                t.add("核心内无资源");
                return;
            }
            for (int i = 0; i < Vars.content.items().size; i++) {
                Item item = Vars.content.item(i);
                if (!sharded.items().has(item)) continue;
                t.image(item.uiIcon).tooltip(tt -> tt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel)).margin(2f);
                t.add(UI.formatAmount(core.items.get(item))).minWidth(5 * 8f).left();
                t.add("->");
                t.image(item.uiIcon).tooltip(tt -> tt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel)).margin(2f);
                t.add(UI.formatAmount(core.storageCapacity)).minWidth(5 * 8f).left();
                t.row();
            }
            t.row();
            t.button("确认", Core::click_yes).size(120f, 50f);
            t.button("取消", Core::click_no).size(120f, 50f);
        });
    }

    public static void resetItemTable() {
        itemTable.clearChildren();
    }

    public static void click_no() {
        resetItemTable();
        dialog_yes.hide();
    }

    public static void click_yes() {
        changeItems();
        click_no();
    }

    public static void changeItems() {
        CoreBlock.CoreBuild core = sharded.core();
        if (core == null || core.items == null) {
            return;
        }
        for (int i = 0; i < Vars.content.items().size; i++) {
            Item item = Vars.content.item(i);
            if (!sharded.items().has(item)) continue;
            core.items.set(item, core.storageCapacity);
        }
    }
}