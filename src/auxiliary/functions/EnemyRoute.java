package auxiliary.functions;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.ai.Pathfinder;
import mindustry.ai.types.FlyingAI;
import mindustry.ai.types.SuicideAI;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Unit;
import mindustry.world.Tile;

import static mindustry.Vars.pathfinder;
import static mindustry.Vars.state;

public class EnemyRoute extends Function {
    boolean isOpen = false;

    public EnemyRoute() {
        super(0, new Table(table -> table.add("敌人进攻路线")));

        Events.run(EventType.Trigger.draw, () -> {
            if (isOpen) {
                draw();
                Draw.reset();
            }
        });
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isOpen));
            box.changed(() -> isOpen = !isOpen);
            t.add(box);
        });
    }

    public void draw() {
        for (Unit unit : state.rules.waveTeam.data().units) {
            if (!unit.type.flying) {
                Lines.stroke(1, Color.acid);
                Seq<Tile> pathTiles = generatePathTiles(unit.tileOn(), unit.team, unit.controller() instanceof SuicideAI ? 0 : unit.pathType());
                for (int i = 0; i < pathTiles.size - 1; i++) {
                    Tile from = pathTiles.get(i), to = pathTiles.get(i + 1);
                    if (from == null || to == null) continue;
                    Lines.line(from.worldx(), from.worldy(), to.worldx(), to.worldy());
                }
            } else {
                Lines.stroke(1, Color.red);
                if (unit.controller() instanceof FlyingAI) {
                    for (var mount : unit.mounts) {
                        if (mount.target != null) {
                            Lines.line(unit.x, unit.y, mount.target.getX(), mount.target.getY());
                        }
                    }

                }
            }
        }
    }

    public static Seq<Tile> generatePathTiles(Tile startTile, Team team, int type) {
        Seq<Tile> pathTiles = new Seq<>();

        getNextTile(startTile, pathfinder.getField(team, type, Pathfinder.fieldCore), pathTiles);

        return pathTiles;
    }

    static void getNextTile(Tile tile, Pathfinder.Flowfield field, Seq<Tile> pathTiles) {
        Tile nextTile = pathfinder.getTargetTile(tile, field);
        pathTiles.add(nextTile);
        if (nextTile == tile || nextTile == null) return;
        getNextTile(nextTile, field, pathTiles);
    }
}
