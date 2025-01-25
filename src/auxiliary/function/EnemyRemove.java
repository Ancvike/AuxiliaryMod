package auxiliary.function;

import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Icon;

public class EnemyRemove extends Function {
    public EnemyRemove() {
        super("enemy-remove", Icon.eraser, "清除废墟");
    }

    @Override
    public void onClick() {
        for (Building building : Team.derelict.data().buildings) {
            building.kill();
        }
    }
}
