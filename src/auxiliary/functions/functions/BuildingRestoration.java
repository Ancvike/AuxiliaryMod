package auxiliary.functions.functions;

import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.gen.Building;

import static auxiliary.functions.Menu.dialog;

public class BuildingRestoration extends Function {
    public BuildingRestoration() {
        super(0, new Table(table -> table.add("建筑修复")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            for (Building building : Vars.player.team().data().buildings) {
                building.health = building.maxHealth;
            }
            dialog.hide();
            Vars.ui.hudfrag.showToast("已修复所有建筑");
        }).width(200f));
    }
}