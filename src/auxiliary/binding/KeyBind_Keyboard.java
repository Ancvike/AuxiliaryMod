package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Unit;
import mindustry.input.Placement;

import static mindustry.Vars.player;

public class KeyBind_Keyboard extends Table {
    public static boolean isUnit = false;
    public static boolean isTap = false;
    int startX, startY;
    int endX, endY;
    Table table = new Table();

    public void init() {
        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode) && isTap) {
                endX = World.toTile(Core.input.mouseWorld().x);
                endY = World.toTile(Core.input.mouseWorld().y);

                Placement.NormalizeDrawResult result = Placement.normalizeDrawArea(Blocks.air, startX, startY, endX, endY, false, 64, 1f);

                Lines.stroke(2f);

                Draw.color(Color.green);
                Lines.rect(result.x, result.y - 1, result.x2 - result.x, result.y2 - result.y);
                Draw.color(Color.acid);
                Lines.rect(result.x, result.y, result.x2 - result.x, result.y2 - result.y);

                for (Building building : player.team().data().buildings) {
                    table.add(building.getDisplayName() + building.x + "," + building.y).row();
                }
            }
        });

        Vars.ui.hudGroup.fill(t-> {
            t.add(table).left();
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                startX = World.toTile(Core.input.mouseWorld().x);
                startY = World.toTile(Core.input.mouseWorld().y);
                isTap = true;
                for (Building building : player.team().data().buildings) {
                    table.add(building.getDisplayName() + building.x + "," + building.y).row();
                }
            }
        });

        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                //实现方法
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
                isTap = false;
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode && !isUnit) {
                Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                for (Unit unit : selectedUnits) {
                    unit.health = unit.maxHealth;
                }
                Vars.ui.hudfrag.showToast("所选单位已修复");
                isUnit = true;
            } else if (Core.input.keyRelease(MyKeyBind.RECOVERY_UNIT.nowKeyCode)) {
                isUnit = false;
            }
        });
    }

    private boolean isZone(Building building) {
        return ((building.x >= startX && building.x <= endX) || (building.x <= startX && building.x >= endX)) && ((building.y >= startY && building.y <= endY) || (building.y <= startY && building.y >= endY));
    }
}