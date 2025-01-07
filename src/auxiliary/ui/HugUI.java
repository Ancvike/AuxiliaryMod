package auxiliary.ui;

import auxiliary.function.BuildingRestoration;
import auxiliary.function.FullResource;
import auxiliary.function.UIMovement;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.name = "ui-move";
            t.button(Icon.menu, UIMovement::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "移动UI").pad(2f);
            });
            t.x = 350;
            t.top();
        });
        Vars.ui.hudGroup.fill(t -> {
            t.name = "full-resource";
            t.button(Icon.fill, FullResource::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "资源全满").pad(2f);
            });
            t.x = 400;
            t.top();
        });
//        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find(""));
        Vars.ui.hudGroup.fill(t -> {
            t.name = "building-restoration";
            t.button(Icon.refresh1, BuildingRestoration::onClick).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "建筑修复").pad(2f);
            });
            t.top();
            t.x = 450;
        });
    }
}
