package auxiliary.ui;

import auxiliary.function.BuildingRestoration;
import auxiliary.function.FullResource;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static auxiliary.tables.Tables.UITable;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.top();
            t.x = 400;
            t.button(Icon.fill, FullResource::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "资源全满").pad(2f);
            });

        });
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
