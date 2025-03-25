package auxiliary.functions;

import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.state;

public class BuildingRestoration extends Function {
    public BuildingRestoration() {
        super(1, "建筑修复");
    }

    @Override
    public void onClick() {
        if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
            for (Building building : Vars.player.team().data().buildings) {
                building.health = building.maxHealth;
            }
            dialog.hide();
            Vars.ui.hudfrag.showToast("已修复所有建筑");
        } else {
            dialog.hide();
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
    }
}