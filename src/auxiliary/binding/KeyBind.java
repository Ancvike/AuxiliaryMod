package auxiliary.binding;

import static mindustry.Vars.mobile;

public class KeyBind {
    public static KeyBind_Keyboard keyBindKeyboard = new KeyBind_Keyboard();
    public static void init() {
        if (mobile) {
            new KeyBind_Mobile().init();
        } else {
            keyBindKeyboard.init();
        }
    }
}
