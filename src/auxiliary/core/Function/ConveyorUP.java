package auxiliary.core.Function;

import arc.Core;

import static auxiliary.core.binding.MyKeyBind.UP;

public class ConveyorUP {

    public static void init() {

    }

    static void pollInput() {
        if (Core.input.keyTap(UP.nowKeyCode)) {

        }
    }
}