package auxiliary.functions;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;

import static auxiliary.functions.Menu.dialog;

public class DerelictRemove extends Function {

    public DerelictRemove() {
        super(1, new Table(table -> table.add("清除废墟")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if (Vars.state.rules.sector.isCaptured() || Vars.state.rules.mode() == Gamemode.sandbox) {
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
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }
}
