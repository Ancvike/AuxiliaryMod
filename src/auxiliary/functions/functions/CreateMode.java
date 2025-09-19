package auxiliary.functions.functions;

import arc.Core;
import arc.scene.ui.*;
import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

public class CreateMode extends Function {
    Table createModeTable = new Table();

    public CreateMode() {
        super(0, new Table(table -> table.add("自由改造").tooltip("你可以自由地在地图上修改一切")));

        createModeTable.setWidth(300f);
        createModeTable.setHeight(300f);
        createModeTable.table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("自由改造");
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> createModeTable.visible = false).grow()).maxWidth(8 * 15f).growY();
        });
    }


    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(createModeTable.visible));
            box.changed(() -> createModeTable.visible = !createModeTable.visible);
            t.add(box);
        });
    }
}