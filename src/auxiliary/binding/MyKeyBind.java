package auxiliary.binding;

import arc.input.KeyCode;

public enum MyKeyBind {
    CONVEYOR_CHANGE("传送带升级",KeyCode.u),
    RECOVERY_BUDDING("建筑修复", KeyCode.i),
    RECOVERY_UNIT("单位修复", KeyCode.o),
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
