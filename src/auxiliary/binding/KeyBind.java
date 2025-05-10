package auxiliary.binding;

import arc.Core;
import arc.graphics.Color;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.gen.Building;
import mindustry.graphics.Drawf;
import mindustry.input.InputHandler;

import static mindustry.Vars.player;

public class KeyBind extends InputHandler {
    int startX, endX, startY, endY;
    boolean isTap = false;

    void setupMobileEvents() {
    }

    void setupDesktopEvents() {
    }

    void handleSelectionDraw(Color color1, Color color2) {
        player.shooting = false;
        endX = World.toTile(Core.input.mouseWorld().x);
        endY = World.toTile(Core.input.mouseWorld().y);
        drawSelection(startX, startY, endX, endY, 64, color1, color2);

        for (Building building : player.team().data().buildings) {
            if (inZone(building)) {
                Drawf.selected(building, color2);
            }
        }
    }

    void startSelection() {
        player.shooting = false;
        startX = World.toTile(Core.input.mouseWorld().x);
        startY = World.toTile(Core.input.mouseWorld().y);
        isTap = true;
    }

    void handleSelectionEnd() {
    }

    boolean shouldHandleInput() {
        return !Vars.ui.chatfrag.shown() && // 未打开聊天框
                Core.scene.getKeyboardFocus() == null && // 无文本输入焦点
                Vars.ui.hudfrag.shown && // HUD正常显示
                Vars.state.isPlaying() && // 游戏进行中
                Vars.player != null; // 玩家实体存在
    }

    boolean inZone(Building building) {
        int x = World.toTile(building.x);
        int y = World.toTile(building.y);
        return ((x >= startX && x <= endX) || (x <= startX && x >= endX)) && ((y >= startY && y <= endY) || (y <= startY && y >= endY));
    }

    void resetSelection() {
        startX = startY = endX = endY = 0;
        isTap = false;
    }
}
