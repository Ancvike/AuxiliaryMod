package auxiliary.ui;

import auxiliary.core.BuildingRestoration;
import auxiliary.core.FullResource;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.button(Icon.fill, FullResource::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "资源全满").pad(2f);
            });
            t.top();
            t.x = 300;
        });
        Vars.ui.hudGroup.fill(t -> {
            t.button(Icon.fill, BuildingRestoration::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "建筑修复").pad(2f);
            });
            t.top();
            t.x = 320;
        });
    }
}
