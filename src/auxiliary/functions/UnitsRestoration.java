package auxiliary.functions;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.gen.Unit;

import static auxiliary.functions.Menu.dialog;

public class UnitsRestoration extends Function {
    public UnitsRestoration() {
        super(1, new Table(table -> table.add("单位修复")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if (Vars.state.rules.sector.isCaptured() || Vars.state.rules.mode() == Gamemode.sandbox) {
                for (Unit unit : Vars.player.team().data().units) {
                    unit.health = unit.maxHealth;
                }
                Vars.ui.hudfrag.showToast("已修复所有单位");
                dialog.hide();
            } else {
                dialog.hide();
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }
}