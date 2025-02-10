// 修改后的 AuxiliaryMod.java
package auxiliary;

import arc.Core;
import arc.Events;
import arc.scene.ui.Button;
import arc.scene.ui.Dialog;
import arc.util.Align;
import mindustry.mod.Mod;
import mindustry.game.EventType;
import mindustry.game.EventType.WorldLoadEvent;

public class AuxiliaryMod extends Mod {

    private Button gameButton; // 将按钮声明为类变量，方便后续管理

    @Override
    public void init() {
        // 监听游戏世界加载事件（战役、自定义游戏等）
        Events.on(WorldLoadEvent.class, event -> {
            // 添加按钮到游戏内界面
            addInGameButton();
        });

        // 监听游戏退出事件（返回主菜单时移除按钮）
        Events.on(EventType.ResetEvent.class, event -> {
            if (gameButton != null) {
                gameButton.remove();
                gameButton = null;
            }
        });
    }

    // 在游戏内界面添加按钮
    private void addInGameButton() {
        // 如果按钮已存在，则不再重复添加
        if (gameButton != null || Core.scene == null) return;

        // 创建按钮
        gameButton = new Button();
        gameButton.name = "game-button";
        gameButton.setSize(120f, 40f);
        gameButton.setPosition(
                Core.graphics.getWidth() - 20f, // 右侧距离屏幕边缘20像素
                20f,                            // 顶部距离屏幕边缘20像素
                Align.topRight                  // 对齐方式：右上角
        );
        gameButton.add("Auxiliary");

        // 点击事件：打开对话框
        gameButton.clicked(() -> Core.app.post(this::showCustomDialog));

        // 将按钮添加到游戏界面
        Core.scene.add(gameButton);
    }

    // 对话框逻辑（保持不变）
    private void showCustomDialog() {
        Dialog dialog = new Dialog();
        dialog.title.setText("Game Menu");
        dialog.cont.add("This is a dialog in-game!").pad(20f).row();
        dialog.buttons.button("Close", dialog::hide).size(100f, 40f);
        dialog.show();
    }
}