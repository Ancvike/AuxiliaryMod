package auxiliary.ui;

import auxiliary.function.UIMovement;
import mindustry.Vars;

import static auxiliary.function.UIMovement.table_Building;
import static auxiliary.function.UIMovement.table_full;

public class HugUI {
    public static void init() {
        UIMovement.init();
        Vars.ui.hudGroup.addChild(table_full);

        Vars.ui.hudGroup.addChild(table_Building);
    }
}
