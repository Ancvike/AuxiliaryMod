package auxiliary.functions;

import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.gen.Unit;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.state;

public class UnitsRestoration extends Function {
    public UnitsRestoration() {
        super(0, "单位修复");
    }

    @Override
    public void onClick() {
        if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
            for (Unit unit : Vars.player.team().data().units) {
                unit.health = unit.maxHealth;
            }
            Vars.ui.hudfrag.showToast("已修复所有单位");
            dialog.hide();
        } else {
            dialog.hide();
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
    }
}