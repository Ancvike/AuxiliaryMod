package auxiliary.binding;

import arc.input.KeyCode;

public enum MyKeyBind {
    UP(KeyCode.u, KeyCode.u);
    private final KeyCode defaultKeyCode;
    public KeyCode nowKeyCode;

    MyKeyBind(KeyCode defaultValue, KeyCode nowKeyCode){
        this.defaultKeyCode = defaultValue;
        this.nowKeyCode = nowKeyCode;
    }

    public static void init() {

    }

    public static void update(MyKeyBind key, KeyCode keyCode) {
        key.nowKeyCode = keyCode;
    }

    public static void resetKeyBind(MyKeyBind key) {
        key.nowKeyCode = key.defaultKeyCode;
    }
}
