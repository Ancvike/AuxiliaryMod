package auxiliary.ui;

import mindustry.Vars;

import static auxiliary.function.UIMovement.table_Building;
import static auxiliary.function.UIMovement.table_full;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.addChild(table_full);

        Vars.ui.hudGroup.addChild(table_Building);
    }
}
