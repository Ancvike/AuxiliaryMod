package auxiliary.function;

import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;

public class EnemyRemove extends Function {
    int a;

    public EnemyRemove() {
        super("enemy-remove", Icon.eraser, "清除废墟");
    }

    @Override
    public void onClick() {
        Seq<Building> buildings = Team.derelict.data().buildings;
        for (Building building : buildings) {
            a++;
            building.kill();
        }
        Vars.ui.hudfrag.showToast(a + "");
        a = 0;
    }
}
