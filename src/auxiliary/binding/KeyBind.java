package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.input.KeyCode;
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
import mindustry.input.InputHandler;
import mindustry.input.Placement;
import mindustry.ui.Styles;

import static mindustry.Vars.*;

public class KeyBind extends InputHandler {
    // 状态变量
    private boolean isTap = false;
    private int startX, startY, endX, endY;
    private boolean isUnitTrue = false;
    private int count = 0;
    public static boolean isOpen = false;

    public void init() {
        // 移动端逻辑
        if (mobile) {
            setupMobileEvents();
        } else {
            setupDesktopEvents();
        }
    }

    // 移动端事件监听
    private void setupMobileEvents() {
        // 框选绘制
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyDown(KeyCode.mouseLeft) && isTap) {
                handleSelectionDraw();
            }
        });

        // 框选开始
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(KeyCode.mouseLeft)) {
                startSelection();
            }
        });

        // 框选结束
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyRelease(KeyCode.mouseLeft)) {
                handleSelectionEnd();
            }
        });

        // 单位修复按钮
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

    // 桌面端事件监听
    private void setupDesktopEvents() {
        // 框选绘制
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode) && isTap) {
                handleDesktopSelectionDraw();
            }
        });

        // 框选开始
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                startSelection();
            }
        });

        // 框选结束
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                handleDesktopSelectionEnd();
            }
        });

        // 单位修复
        Events.run(EventType.Trigger.update, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode) {
                healSelectedUnits();
            }
        });
    }

    // 输入状态验证
    private boolean shouldHandleInput() {
        return isOpen && // 功能开关
            !Vars.ui.chatfrag.shown() && // 未打开聊天框
            Core.scene.getKeyboardFocus() == null && // 无文本输入焦点
            Vars.ui.hudfrag.shown && // HUD正常显示
            Vars.state.isPlaying() && // 游戏进行中
            Vars.player != null; // 玩家实体存在
    }

    // 处理框选绘制
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

    // 开始框选
    private void startSelection() {
        player.shooting = false;
        startX = World.toTile(Core.input.mouseWorld().x);
        startY = World.toTile(Core.input.mouseWorld().y);
        isTap = true;
    }

    // 处理框选结束
    private void handleSelectionEnd() {
        if (startX == endX && startY == endY) return;

        for (Building building : player.team().data().buildings) {
            if (isZone(building)) {
                building.health = building.maxHealth;
            }
        }
        Vars.ui.hudfrag.showToast("所选建筑已修复");
        resetSelection();
    }

    // 处理桌面端框选绘制
    private void handleDesktopSelectionDraw() {
        endX = World.toTile(Core.input.mouseWorld().x);
        endY = World.toTile(Core.input.mouseWorld().y);

        Placement.NormalizeDrawResult result = Placement.normalizeDrawArea(
            Blocks.air, startX, startY, endX, endY, false, 64, 1f);

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

    // 处理桌面端框选结束
    private void handleDesktopSelectionEnd() {
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

    // 添加单位修复按钮
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

    // 移除单位修复按钮
    private void removeUnitHealButton() {
        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("mobile-unit"));
    }

    // 修复选中单位
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

    // 判断建筑是否在区域内
    private boolean isZone(Building building) {
        int x = World.toTile(building.x);
        int y = World.toTile(building.y);
        return ((x >= startX && x <= endX) || (x <= startX && x >= endX)) &&
               ((y >= startY && y <= endY) || (y <= startY && y >= endY));
    }

    // 重置选择状态
    private void resetSelection() {
        startX = startY = endX = endY = 0;
        isTap = false;
    }
}