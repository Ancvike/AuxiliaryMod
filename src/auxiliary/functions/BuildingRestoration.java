package auxiliary.functions;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;

import static auxiliary.functions.Menu.dialog;

public class BuildingRestoration extends Function {
    public BuildingRestoration() {
        super(1, new Table(table -> table.add("建筑修复")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
                for (Building building : Vars.player.team().data().buildings) {
                    building.health = building.maxHealth;
                }
                dialog.hide();
                Vars.ui.hudfrag.showToast("已修复所有建筑");
            } else {
                dialog.hide();
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }
}