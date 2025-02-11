package auxiliary.functions;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.state;

public class DerelictRemove extends Function {
    int a;

    public DerelictRemove() {
        super("清除废墟");
    }

    @Override
    public void onClick() {
//        if (!state.rules.waves && state.isCampaign()) {
            Seq<Building> buildings = Team.derelict.data().buildings;
            for (Building building : buildings) {
                a++;
                building.kill();
            }
            Vars.ui.hudfrag.showToast(a + "");
            a = 0;
            dialog.hide();
//        } else {
//            dialog.hide();
//            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
//        }
    }
}
