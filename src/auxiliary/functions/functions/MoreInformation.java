package auxiliary.functions.functions;

import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

public class MoreInformation extends Function {
    boolean isOpen = false;

    public MoreInformation() {
        super(0, new Table(table -> table.add("更多的信息显示").tooltip("单位血量, 单位护盾值, 建筑血量, 炮塔弹药, 工厂生产进度")));

        Events.run(EventType.Trigger.draw, () -> {
            if (isOpen) {
                for (Tile tile : Vars.world.tiles) {
                    onTile(tile);
                    Draw.reset();
                }
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (isOpen) {
                for (Unit unit : Groups.unit) {
                    onUnit(unit);
                    Draw.reset();
                }
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

    public void onTile(Tile tile) {
        if (tile.build == null) return;

        Building b = tile.build;

        drawBar(b, -(b.block.size * 4 - 2), b.healthf(), Pal.health);

        if (b instanceof Turret.TurretBuild turretBuild) {
            drawBar(b, b.block.size * 4 - 2, turretBuild.reloadCounter / ((Turret) b.block).reload, Pal.ammo);
        }
        if (b instanceof ConstructBlock.ConstructBuild constructBuild)
            drawBar(b, b.block.size * 4 - 2, constructBuild.progress(), b.team.color);
        if (b instanceof Reconstructor.ReconstructorBuild reconstructorBuild)
            drawBar(b, b.block.size * 4 - 2, reconstructorBuild.fraction(), b.team.color);
        if (b instanceof UnitFactory.UnitFactoryBuild factoryBuild)
            drawBar(b, b.block.size * 4 - 2, factoryBuild.fraction(), b.team.color);

    }

    public void onUnit(Unit unit) {
        drawBar(unit, -(unit.type.hitSize - 5f), unit.healthf(), Pal.health);
    }

    void drawBar(Building b, float offsetY, float progress, Color color) {
        float bx = b.x, by = b.y + offsetY;
        float width = b.block.size * 7.5f, height = 2;
        Draw.color(Pal.gray);
        Fill.quad(
                bx - width / 2, by + height / 2,
                bx - width / 2, by - height / 2,
                bx + width / 2, by - height / 2,
                bx + width / 2, by + height / 2);
        Draw.color(color);
        width = b.block.size * 7.5f - 0.5f;
        height = 2 - 0.5f;
        Fill.quad(
                bx - width / 2, by + height / 2,
                bx - width / 2, by - height / 2,
                bx - width / 2 + width * progress, by - height / 2,
                bx - width / 2 + width * progress, by + height / 2);
    }

    void drawBar(Unit u, float offsetY, float progress, Color color) {
        float bx = u.x, by = u.y + offsetY;
        float width = u.type.hitSize + 4f, height = 2;
        Draw.color(Pal.gray);
        Fill.quad(
                bx - width / 2, by + height / 2,
                bx - width / 2, by - height / 2,
                bx + width / 2, by - height / 2,
                bx + width / 2, by + height / 2);
        Draw.color(color);
        width = u.type.hitSize + 3f;
        height = 2 - 0.5f;
        Fill.quad(
                bx - width / 2, by + height / 2,
                bx - width / 2, by - height / 2,
                bx - width / 2 + width * progress, by - height / 2,
                bx - width / 2 + width * progress, by + height / 2);
    }
}
