package auxiliary.ui;

import arc.scene.ui.layout.Table;
import auxiliary.function.BuildingRestoration;
import auxiliary.function.FullResource;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static auxiliary.function.UIMovement.table_full;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.addChild(table_full);

        Vars.ui.hudGroup.fill(t -> {
            t.top();
            t.x = 450;
            t.button(Icon.refresh1, BuildingRestoration::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "建筑修复").pad(2f);
            });
        });
    }
}
