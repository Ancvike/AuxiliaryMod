package auxiliary.function;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static auxiliary.dialogs.Dialogs.*;
import static mindustry.Vars.state;

public class FullResource extends Function {
    private static final BaseDialog dialog_full = new BaseDialog("确认页面");
    private Table itemsTable;

    public FullResource() {
        super("full-resource", Icon.fill, "资源全满");
    }

    @Override
    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            itemsTable = setItemsTable();
            setDialog_yes(dialog_full, itemsTable);
            dialog_full.show();
        } else {
            dialog_no.show();
        }
    }

    public Table setItemsTable() {
        return new Table(t -> {
            t.add("资源列表").row();
            CoreBlock.CoreBuild core = Vars.player.team().core();
            if (core == null || core.items == null) {
                t.add("核心内无资源");
                return;
            }
            for (int i = 0; i < Vars.content.items().size; i++) {
                Item item = Vars.content.item(i);
                if (!Vars.player.team().items().has(item)) continue;
                t.image(item.uiIcon).tooltip(tt -> tt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel)).margin(2f);
                t.add(UI.formatAmount(core.items.get(item))).minWidth(5 * 8f).left();
                t.add("->");
                t.image(item.uiIcon).tooltip(tt -> tt.background(Styles.black6).add(item.localizedName).style(Styles.outlineLabel)).margin(2f);
                t.add(UI.formatAmount(core.storageCapacity)).minWidth(5 * 8f).left();
                t.row();
            }
            t.row();
            t.button("确定", this::click_yes).size(120f, 50f);
            t.button("取消", this::click_no).size(120f, 50f);
        });
    }

    private void click_no() {
        itemsTable.clearChildren();
        dialog_full.hide();
    }

    private void click_yes() {
        changeItems();
        click_no();
    }

    private void changeItems() {
        CoreBlock.CoreBuild core = Vars.player.team().core();
        if (core == null || core.items == null) {
            return;
        }
        for (int i = 0; i < Vars.content.items().size; i++) {
            Item item = Vars.content.item(i);
            if (!Vars.player.team().items().has(item)) continue;
            core.items.set(item, core.storageCapacity);
        }
    }
}