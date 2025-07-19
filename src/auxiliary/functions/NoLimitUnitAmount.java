package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class NoLimitUnitAmount extends Function {
    boolean isOpen = false;
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
                    Vars.state.rules.unitCap = 999;
                } else {
                    Vars.state.rules.unitCap = pre_unitCap;
                }
            });
            t.add(box);
        });
    }
}