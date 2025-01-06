package auxiliary.function;

import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.core.UI;
import mindustry.type.Item;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.world.blocks.storage.CoreBlock;

import static auxiliary.dialogs.Dialogs.*;
import static mindustry.Vars.state;
import static mindustry.game.Team.sharded;

public class FullResource {
    private static final BaseDialog dialog_full = new BaseDialog("确认页面");
    private static Table itemsTable;

    public static void init() {
    }

    public static void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            itemsTable = setTable();
            setDialog_yes(dialog_full, itemsTable);
            dialog_full.show();
        } else {
            dialog_no.show();
        }
    }

    private static Table setTable() {
        return new Table(t -> {
            t.add("调整当前图标的位置").row();
            t.add("X:");
            Slider sliderX = new Slider(0, 500, 1, false);
            t.add(sliderX.getValue() + "").row();
            t.add("Y:");
            Slider sliderY = new Slider(0, 500, 1, false);
            t.add(sliderY.getValue() + "").row();
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
            t.button("确定", FullResource::click_yes).size(120f, 50f);
            t.button("取消", FullResource::click_no).size(120f, 50f);
        });
    }

    private static void resetItemsTable() {
        itemsTable.clearChildren();
    }

    private static void click_no() {
        resetItemsTable();
        dialog_full.hide();
    }

    private static void click_yes() {
        changeItems();
        click_no();
    }

    private static void changeItems() {
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

    public static void changeSlider(Slider slider) {
//        slider.changed(() -> {
//            label.setText("Block Health: "+(int)slider.getValue()+"%");
//        });
//        slider.change();
//        slider.moved(hp->Groups.build.each(b->b.health(b.block.health*hp/100)));
    }
}