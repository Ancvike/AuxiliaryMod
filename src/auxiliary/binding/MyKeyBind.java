package auxiliary.binding;

import arc.input.KeyCode;

public enum MyKeyBind {
    RECOVERY_BUDDING("框选建筑修复", KeyCode.u),
    RECOVERY_UNIT("单位修复", KeyCode.i),
    OPEN_MENU("打开面板", KeyCode.o),
    ;
    private final String name;
    private final KeyCode defaultKeyCode;
    public KeyCode nowKeyCode;

    MyKeyBind(String name, KeyCode defaultKeyCode) {
        this.name = name;
        this.defaultKeyCode = defaultKeyCode;
        this.nowKeyCode = defaultKeyCode;
    }

    public static void update(MyKeyBind key, KeyCode keyCode) {
        key.nowKeyCode = keyCode;
    }

    public static void resetKeyBind(MyKeyBind key) {
        key.nowKeyCode = key.defaultKeyCode;
    }

    public String getName() {
        return name;
    }
}