package auxiliary.functions.functions;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Fill;
import arc.graphics.gl.FrameBuffer;
import arc.math.Angles;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.util.Align;
import arc.util.Tmp;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.entities.abilities.Ability;
import mindustry.entities.abilities.ForceFieldAbility;
import mindustry.game.EventType;
import mindustry.game.Team;
import mindustry.gen.Building;
import mindustry.gen.Groups;
import mindustry.gen.Unit;
import mindustry.graphics.Pal;
import mindustry.ui.Fonts;
import mindustry.world.Tile;
import mindustry.world.blocks.ConstructBlock;
import mindustry.world.blocks.defense.turrets.ItemTurret;
import mindustry.world.blocks.defense.turrets.LiquidTurret;
import mindustry.world.blocks.defense.turrets.PowerTurret;
import mindustry.world.blocks.defense.turrets.Turret;
import mindustry.world.blocks.units.Reconstructor;
import mindustry.world.blocks.units.UnitFactory;

import static arc.Core.graphics;
import static arc.graphics.g2d.Draw.xscl;
import static arc.graphics.g2d.Draw.yscl;
import static mindustry.Vars.maxBlockSize;
import static mindustry.Vars.renderer;

public class MoreInformation extends Function {
    boolean isOpen = false;

    static final float[] zIndexTeamCache = new float[Team.baseTeams.length];
    static final FrameBuffer effectBuffer = new FrameBuffer();

    public MoreInformation() {
        super(0, new Table(table -> table.add("更多的信息显示").tooltip("单位血量, 单位护盾值, 建筑血量, 炮塔弹药, 工厂生产进度")));

        for (int i = 0; i < Team.baseTeams.length; i++) {
            zIndexTeamCache[i] = 166 + (Team.baseTeams.length - Team.baseTeams[i].id) * 3 * 0.001f;
        }

        Events.run(EventType.Trigger.draw, () -> {
            if (!isOpen) return;
            Core.camera.bounds(Tmp.r1);

            effectBuffer.resize(graphics.getWidth(), graphics.getHeight());
            for (float zIndex : zIndexTeamCache) {
                Draw.drawRange(zIndex, () -> effectBuffer.begin(Color.clear), effectBuffer::end);
            }

            for (Tile tile : Vars.world.tiles) {
                onTile(tile);
                Draw.reset();
            }

            Draw.reset();
        });

        Events.run(EventType.Trigger.draw, () -> {
            if (!isOpen) return;
            Core.camera.bounds(Tmp.r1);

            for (Unit unit : Groups.unit) {
                onUnit(unit);
                Draw.reset();
            }

            Draw.reset();
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
        if (!isInCamera(tile.worldx(), tile.worldy(), 8) || tile.build == null) return;

        Building b = tile.build;

        drawBar(b, -(b.block.size * 4 - 2), b.healthf(), Pal.health);

        if (b instanceof Turret.TurretBuild turretBuild) {
            if (b.block instanceof ItemTurret) {
                drawBar(b, b.block.size * 4 - 2, (float) turretBuild.totalAmmo / ((ItemTurret) b.block).maxAmmo, Pal.ammo);

                if (turretBuild.hasAmmo()) {
                    Draw.reset();
                    if (b.block.size == 1) {
                        var region = ((ItemTurret.ItemEntry) turretBuild.ammo.peek()).item.uiIcon;
                        Draw.rect(region, turretBuild.x, turretBuild.y, (float) region.width * region.scl() * xscl / 2, (float) region.height * region.scl() * yscl / 2);
                    } else
                        Draw.rect(((ItemTurret.ItemEntry) turretBuild.ammo.peek()).item.uiIcon, turretBuild.x, turretBuild.y);
                }
            }
            if (b.block instanceof LiquidTurret) {
                drawBar(b, b.block.size * 4 - 2, turretBuild.liquids.currentAmount() / b.block.liquidCapacity, Pal.lancerLaser);
                if (turretBuild.liquids.currentAmount() > 0) {
                    Draw.reset();
                    Draw.rect(turretBuild.liquids.current().uiIcon, turretBuild.x, turretBuild.y);
                }
            }
            if (b.block instanceof PowerTurret) drawBar(b, b.block.size * 4 - 2, b.power.status, Pal.ammo);
        }
        if (b instanceof ConstructBlock.ConstructBuild constructBuild)
            drawBar(b, b.block.size * 4 - 2, constructBuild.progress(), b.team.color);
        if (b instanceof Reconstructor.ReconstructorBuild reconstructorBuild)
            drawBar(b, b.block.size * 4 - 2, reconstructorBuild.fraction(), b.team.color);
        if (b instanceof UnitFactory.UnitFactoryBuild factoryBuild)
            drawBar(b, b.block.size * 4 - 2, factoryBuild.fraction(), b.team.color);

    }

    public void onUnit(Unit unit) {
        float maxShield = 1;
        if (isInCamera(unit.x, unit.y, unit.hitSize) && !renderer.pixelator.enabled()) {
            drawBar(unit, -(unit.type.hitSize * 0.9f), unit.healthf(), Pal.health);
            if (unit.shield() > 0) {
                for (Ability ability : unit.abilities) {
                    if (ability instanceof ForceFieldAbility) {
                        maxShield = ((ForceFieldAbility) ability).max;
                    }
                }

                drawBar(unit, unit.type.hitSize * 0.9f, unit.shield / maxShield, Pal.shield);
            }

            if (unit.item() != null && unit.itemTime > 0.01f) {
                Fonts.outline.draw(String.valueOf(unit.stack.amount), unit.x + Angles.trnsx(unit.rotation + 180f, unit.type.itemOffsetY), unit.y + Angles.trnsy(unit.rotation + 180f, unit.type.itemOffsetY) - 3, Pal.accent, 0.25f * unit.itemTime / Scl.scl(1f), false, Align.center);
            }
        }
    }

    void drawBar(Building b, float offsetY, float progress, Color color) {
        float bx = b.x, by = b.y + offsetY;
        float width = b.block.size * 7.5f, height = 2;
        Draw.color(Pal.gray);
        Fill.quad(bx - width / 2, by + height / 2, bx - width / 2, by - height / 2, bx + width / 2, by - height / 2, bx + width / 2, by + height / 2);
        Draw.color(color);
        width = b.block.size * 7.5f - 0.5f;
        height = 2 - 0.5f;
        Fill.quad(bx - width / 2, by + height / 2, bx - width / 2, by - height / 2, bx - width / 2 + width * progress, by - height / 2, bx - width / 2 + width * progress, by + height / 2);
    }

    void drawBar(Unit u, float offsetY, float progress, Color color) {
        float bx = u.x, by = u.y + offsetY;
        float width = u.type.hitSize + 4f, height = 2;
        Draw.color(Pal.gray);
        Fill.quad(bx - width / 2, by + height / 2, bx - width / 2, by - height / 2, bx + width / 2, by - height / 2, bx + width / 2, by + height / 2);
        Draw.color(color);
        width = u.type.hitSize + 3f;
        height = 2 - 0.5f;
        Fill.quad(bx - width / 2, by + height / 2, bx - width / 2, by - height / 2, bx - width / 2 + width * progress, by - height / 2, bx - width / 2 + width * progress, by + height / 2);
    }

    public static boolean isInCamera(float x, float y, float size) {
        Tmp.r2.setCentered(x, y, size);
        return Tmp.r1.overlaps(Tmp.r2);
    }
}