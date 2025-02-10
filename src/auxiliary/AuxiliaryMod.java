package auxiliary;

import arc.Core;
import arc.Events;
import arc.scene.ui.Button;
import arc.scene.ui.Dialog;
import arc.util.Align;
import mindustry.mod.Mod;
import mindustry.game.EventType;

public class AuxiliaryMod extends Mod {

    @Override
    public void init() {
        // 监听游戏更新事件，确保按钮只添加一次
        Events.run(EventType.Trigger.update, () -> {
            if (Core.scene != null && Core.scene.find("auxiliary-button") == null) {
                addCustomButton();
            }
        });
    }

    // 添加自定义按钮到游戏界面
    private void addCustomButton() {
        // 创建按钮
        Button button = new Button();
        button.name = "auxiliary-button"; // 唯一标识符
        button.setSize(120f, 40f);        // 宽度, 高度
        button.setPosition(20f, 20f, Align.bottomLeft); // 位置（左下角）
        button.add("Auxiliary"); // 按钮文本

        // 点击事件：打开对话框
        button.clicked(() -> {
            Core.app.post(this::showCustomDialog); // 确保在主线程执行
        });

        // 添加到游戏界面
        Core.scene.add(button);
    }

    // 显示自定义对话框
    private void showCustomDialog() {
        Dialog dialog = new Dialog();
        dialog.title.setText("Auxiliary Menu"); // 对话框标题
        // 添加内容
        dialog.cont.add("Welcome to Auxiliary Mod!").pad(20f).row();
        // 示例：添加一个关闭按钮
        dialog.buttons.button("Close", dialog::hide).size(100f, 40f);
        // 显示对话框
        dialog.show();
    }
}
