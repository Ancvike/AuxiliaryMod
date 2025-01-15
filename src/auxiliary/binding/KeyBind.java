package auxiliary.binding;

import static mindustry.Vars.mobile;

public class KeyBind {
    public static final KeyBind_Keyboard keyBind_Keyboard = new KeyBind_Keyboard();
    public static void init() {
        if (mobile) {
            new KeyBind_Mobile().init();
        } else {
            keyBind_Keyboard.init();
        }
    }
}
