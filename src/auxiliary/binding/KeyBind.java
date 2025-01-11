package auxiliary.binding;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;

public class KeyBind {
    private static boolean dialogShown = false;  // 添加一个标志变量来跟踪对话框是否已显示

    public static void init() {

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (input.keyDown(CONVEYOR_CHANGE.nowKeyCode) && !dialogShown) {  // 检查键是否被按下且对话框未显示
                BaseDialog dialog = new BaseDialog("传送带升级");
                dialog.addCloseButton();
                dialog.show();
                dialogShown = true;  // 设置对话框已显示的标志
            } else if (input.keyRelease(CONVEYOR_CHANGE.nowKeyCode)) {  // 检查键是否被释放
                dialogShown = false;  // 重置对话框已显示的标志
            }
        });

    }
}