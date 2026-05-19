package auxiliary.functions.functions;

import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;

import static auxiliary.functions.Menu.dialog;

public class DerelictRemove extends Function {

    public DerelictRemove() {
        super(0, new Table(table -> table.add("清除废墟")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
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
        }).width(200f));
    }
}
