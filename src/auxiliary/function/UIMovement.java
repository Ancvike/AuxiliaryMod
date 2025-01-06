package auxiliary.function;

import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class UIMovement {
    public static Table table_full = new Table();
    public static Table table_Building = new Table();

    public static void init() {
        table_full.button(Icon.fill, FullResource::onClick).size(40f).tooltip(t -> {
            t.setBackground(Styles.black6);
            t.label(() -> "资源全满").pad(2f);
        });
        table_full.x = 400;
        table_full.top();
        table_Building.button(Icon.refresh1, BuildingRestoration::onClick).size(40f).tooltip(t -> {
            t.setBackground(Styles.black6);
            t.label(() -> "建筑修复").pad(2f);

        });
        table_Building.top();
        table_Building.x = 450;
    }
}
