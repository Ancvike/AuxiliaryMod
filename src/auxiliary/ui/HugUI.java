package auxiliary.ui;

import auxiliary.core.Function.BuildingRestoration;
import auxiliary.core.Function.FullResource;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.x = 800;
            t.top();
            t.button(Icon.fill, FullResource::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
//                tt.label(() -> "资源全满").pad(2f);
                tt.label(() -> t.x + "|||" + t.y).pad(2f);
            });

        });
        Vars.ui.hudGroup.fill(t -> {
            t.bottom();
            t.right();
            t.button(Icon.refresh1, BuildingRestoration::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
//                tt.label(() -> "建筑修复").pad(2f);
                tt.label(() -> t.x + "|||" + t.y).pad(2f);
            });

        });
    }
}
