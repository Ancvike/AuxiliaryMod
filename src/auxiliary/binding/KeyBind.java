package auxiliary.binding;

import static mindustry.Vars.mobile;

public class KeyBind {

    public static void init() {
        if (!mobile) {
            new KeyBind_Keyboard().init();
        }
    }
}
