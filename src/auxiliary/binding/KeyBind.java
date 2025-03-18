package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.input.InputHandler;
import mindustry.ui.Styles;

import static mindustry.Vars.*;

public class KeyBind extends InputHandler {
    private boolean isTap = false;
    private int startX, startY, endX, endY;
    private boolean isUnitTrue = false;
    private int count = 0;
    public static boolean isOpen = false;

    private float pressTime = 0f;

    public void init() {
        if (mobile) {
            setupMobileEvents();
        } else {
            setupDesktopEvents();
        }
    }

    private void setupMobileEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isTap && isOpen)) return;

            if (pressTime < 1f) {
                pressTime += Core.graphics.getDeltaTime();
            }

            if (Core.input.keyDown(KeyCode.mouseLeft) && pressTime >= 1f) {
                handleSelectionDraw();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(KeyCode.mouseLeft) && isOpen) {
                startSelection();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isOpen)) return;

            if (Core.input.keyRelease(KeyCode.mouseLeft) && pressTime >= 1f) {
                pressTime = 0f;
                handleSelectionEnd();
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            isUnitTrue = Vars.control.input.commandMode;
            if (isUnitTrue && count == 0) {
                addUnitHealButton();
                count++;
            } else if (!isUnitTrue && count != 0) {
                removeUnitHealButton();
                count = 0;
            }
        });
    }

    private void setupDesktopEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode) && isTap) {
                handleSelectionDraw();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                startSelection();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                handleSelectionEnd();
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode) {
                healSelectedUnits();
            }
        });
    }

    private boolean shouldHandleInput() {
        return !Vars.ui.chatfrag.shown() && // 未打开聊天框
                Core.scene.getKeyboardFocus() == null && // 无文本输入焦点
                Vars.ui.hudfrag.shown && // HUD正常显示
                Vars.state.isPlaying() && // 游戏进行中
                Vars.player != null; // 玩家实体存在
    }

    private void handleSelectionDraw() {
        player.shooting = false;
        endX = World.toTile(Core.input.mouseWorld().x);
        endY = World.toTile(Core.input.mouseWorld().y);
        drawSelection(startX, startY, endX, endY, 64, Color.green, Color.acid);

        for (Building building : player.team().data().buildings) {
            if (isZone(building)) {
                Drawf.selected(building, Color.acid);
            }
        }
    }

    private void startSelection() {
        player.shooting = false;
        startX = World.toTile(Core.input.mouseWorld().x);
        startY = World.toTile(Core.input.mouseWorld().y);
        isTap = true;
    }

    private void handleSelectionEnd() {
        if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
            for (Building building : player.team().data().buildings) {
                if (isZone(building)) {
                    building.health = building.maxHealth;
                }
            }
            Vars.ui.hudfrag.showToast("所选建筑已修复");
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
        resetSelection();
    }

    private void addUnitHealButton() {
        Vars.ui.hudGroup.fill(t -> {
            t.name = "mobile-unit";
            t.bottom().left();
            t.button(Icon.android, () -> {
                Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                for (Unit unit : selectedUnits) {
                    unit.health = unit.maxHealth;
                }
                Vars.ui.hudfrag.showToast("所选单位已修复");
            }).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "单位修复").pad(2f);
            }).left();
            t.row();
            t.table().size(48f);
        });
    }

    private void removeUnitHealButton() {
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("mobile-unit"));
    }

    private void healSelectedUnits() {
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

    private boolean isZone(Building building) {
        int x = World.toTile(building.x);
        int y = World.toTile(building.y);
        return ((x >= startX && x <= endX) || (x <= startX && x >= endX)) && ((y >= startY && y <= endY) || (y <= startY && y >= endY));
    }

    private void resetSelection() {
        startX = startY = endX = endY = 0;
        isTap = false;
    }
}