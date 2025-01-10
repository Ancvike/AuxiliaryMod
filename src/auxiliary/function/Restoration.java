package auxiliary.function;

import arc.scene.ui.layout.Table;
import auxiliary.dialogs.Dialogs;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.dialogs.Dialogs.dialog_no;
import static mindustry.Vars.state;

public class Restoration extends Function {
    private static final BaseDialog dialog_restoration = new BaseDialog("确认页面");

    public Restoration() {
        super("restoration", Icon.refresh1, "一键修复");

        Table table1 = new Table();
        table1.add("你确定要修复所有建筑吗?").size(200f, 50f).row();
        table1.button("确定", this::building_click_yes).size(120f, 50f);
        table1.button("取消", this::click_no).size(120f, 50f);

        Table table2 = new Table();
        table2.row();
        table2.add("你确定要修复所有单位吗?").size(200f, 50f);
        table2.button("确定", this::unit_click_yes).size(120f, 50f);
        table2.button("取消", this::click_no).size(120f, 50f);

        Dialogs.setDialog_yes(dialog_restoration, table1);
        Dialogs.setDialog_yes(dialog_restoration, table2);
    }

    @Override
    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            dialog_restoration.show();
        } else {
            dialog_no.show();
        }
    }

    private void click_no() {
        dialog_restoration.hide();
    }

    private void building_click_yes() {
        restoration_building();
        click_no();
    }

    private void unit_click_yes() {
        restoration_unit();
        click_no();
    }

    private void restoration_building() {
        for (Building building : Team.sharded.data().buildings) {
            building.health = building.maxHealth;
        }
    }

    private void restoration_unit() {
        for (Unit unit : Team.sharded.data().units) {
            unit.health = unit.maxHealth;
        }
    }
}