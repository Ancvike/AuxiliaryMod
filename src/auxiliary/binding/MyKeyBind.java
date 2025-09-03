package auxiliary.binding;

import arc.Core;
import arc.input.KeyCode;

public enum MyKeyBind {
    CHANGE_HP("改变血量", KeyCode.u),
    RECOVERY_UNIT("单位修复", KeyCode.i),
    OPEN_MENU("打开面板", KeyCode.o)
    ;
    private final String name;
    private final KeyCode defaultKeyCode;
    public KeyCode nowKeyCode;

    MyKeyBind(String name, KeyCode defaultKeyCode) {
        this.name = name;
        this.defaultKeyCode = defaultKeyCode;
        this.nowKeyCode = defaultKeyCode;
    }

    public static void setKeyBind(MyKeyBind key, KeyCode keyCode) {
        key.nowKeyCode = keyCode;
        Core.settings.put(key.name, keyCode.name());
    }

    public static void resetKeyBind(MyKeyBind key) {
        key.nowKeyCode = key.defaultKeyCode;
        Core.settings.put(key.name, key.defaultKeyCode.name());
    }

    public String getName() {
        return name;
    }
}