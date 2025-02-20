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
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.input.Placement;

import static mindustry.Vars.*;

public class KeyBind_Keyboard extends Table {
    boolean isTap = false;
    int startX, startY;
    int endX, endY;

    public void init() {
        Events.run(EventType.Trigger.draw, () -> {
            if (Vars.ui.hudfrag.shown || !Vars.ui.minimapfrag.shown()) {
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
                        if (isZone(building)) {
                            Drawf.selected(building, Color.acid);
                        }
                    }
                }
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (Vars.ui.hudfrag.shown || !Vars.ui.minimapfrag.shown()) {
                if (Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                    startX = World.toTile(Core.input.mouseWorld().x);
                    startY = World.toTile(Core.input.mouseWorld().y);
                    isTap = true;
                }
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (Vars.ui.hudfrag.shown || !Vars.ui.minimapfrag.shown()) {
                if (Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode) && state.isPlaying()) {
                    if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
                        for (Building building : player.team().data().buildings) {
                            if (isZone(building)) {
                                building.health = building.maxHealth;
                            }
                        }
                        Vars.ui.hudfrag.showToast("所选建筑已修复");
                        startX = 0;
                        startY = 0;
                        endX = 0;
                        endY = 0;
                        isTap = false;
                    } else {
                        startX = 0;
                        startY = 0;
                        endX = 0;
                        endY = 0;
                        Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
                    }
                }
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (Vars.ui.hudfrag.shown || !Vars.ui.minimapfrag.shown()) {
                if (Core.input.keyTap(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode) {
                    if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
                        Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                        for (Unit unit : selectedUnits) {
                            unit.health = unit.maxHealth;
                        }
                        Vars.ui.hudfrag.showToast("所选单位已修复");
                    } else {
                        Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
                    }
                }
            }
        });
    }

    private boolean isZone(Building building) {
        int x = World.toTile(building.x);
        int y = World.toTile(building.y);
        return ((x >= startX && x <= endX) || (x <= startX && x >= endX)) && ((y >= startY && y <= endY) || (y <= startY && y >= endY));
    }
}