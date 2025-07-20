package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class NoLimitUnitAmount extends Function {
    public static boolean isOpen = false;
    int pre_unitCap;

    public NoLimitUnitAmount() {
        super(1, new Table(t -> t.add("单位数量无限制")));
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isOpen));
            box.changed(() -> {
                isOpen = !isOpen;
                if (isOpen) {
                    pre_unitCap = Vars.state.rules.unitCap;
                    Vars.state.rules.unitCap = 1000;
                } else {
                    Vars.state.rules.unitCap = 0;
                }
            });
            t.add(box);
        });
    }
}