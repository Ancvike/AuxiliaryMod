package auxiliary.function;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.dialogs.BaseDialog;

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
        table2.add("你确定要修复所有单位吗?").size(200f, 50f).row();
        table2.button("确定", this::unit_click_yes).size(120f, 50f);
        table2.button("取消", this::click_no).size(120f, 50f);

        dialog_restoration.cont.add(table1).row();
        dialog_restoration.cont.add(table2);
    }

    @Override
    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            dialog_restoration.show();
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
    }

    private void click_no() {
        dialog_restoration.hide();
    }

    private void building_click_yes() {
        restoration_building();
        click_no();
        Vars.ui.hudfrag.showToast("已修复所有建筑");
    }

    private void unit_click_yes() {
        restoration_unit();
        click_no();
        Vars.ui.hudfrag.showToast("已修复所有单位");
    }

    private void restoration_building() {
        for (Building building : Vars.player.team().data().buildings) {
            building.health = building.maxHealth;
        }
    }

    private void restoration_unit() {
        for (Unit unit : Vars.player.team().data().units) {
            unit.health = unit.maxHealth;
        }
    }
}