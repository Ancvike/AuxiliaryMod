package auxiliary.functions;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.state;

public class DerelictRemove extends Function {

    public DerelictRemove() {
        super(1, 999, "清除废墟");
    }

    @Override
    public Table function() {
        if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
            Seq<Building> buildings = Team.derelict.data().buildings;
            int a = buildings.size;
            while (a != 0) {
                for (Building building : buildings) {
                    a--;
                    building.kill();
                }
            }
            Vars.ui.hudfrag.showToast("清除废墟成功");
            dialog.hide();
        } else {
            dialog.hide();
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
        return null;
    }
}
