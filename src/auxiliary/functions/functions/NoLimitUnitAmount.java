package auxiliary.functions.functions;

import arc.Events;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.game.EventType;

public class NoLimitUnitAmount extends Function {
    public static boolean isOpen = false;
    int pre_unitCap;

    public NoLimitUnitAmount() {
        super(0, new Table(t -> t.add("单位数量无限制")));

        Events.run(EventType.Trigger.update, () -> isOpen = Vars.state.rules.unitCap == 1000);
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
                    Vars.state.rules.unitCap = pre_unitCap;
                }
            });
            t.add(box);
        });
    }
}