package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class NoLimitSchematics extends Function {
    boolean isOpen = false;

    public NoLimitSchematics() {
        super(0, new Table(t -> t.add("蓝图大小无限制")));
    }

    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isOpen));
            box.changed(() -> {
                isOpen = !isOpen;
                if (isOpen) {
                    Vars.maxSchematicSize = 1000;
                } else {
                    Vars.maxSchematicSize = 64;
                }
            });
            t.add(box);
        });
    }
}
