package auxiliary.functions.functions;

import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.gen.Unit;

import static auxiliary.functions.Menu.dialog;

public class UnitsRestoration extends Function {
    public UnitsRestoration() {
        super(0, new Table(table -> table.add("单位修复")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            for (Unit unit : Vars.player.team().data().units) {
                unit.health = unit.maxHealth;
            }
            Vars.ui.hudfrag.showToast("已修复所有单位");
            dialog.hide();
        }).width(200f));
    }
}