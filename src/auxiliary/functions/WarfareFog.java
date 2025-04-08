package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;

import static mindustry.Vars.state;

public class WarfareFog extends Function {

    public WarfareFog() {
        super(0, "战争迷雾");
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(state.rules.fog));
            box.changed(() -> state.rules.fog = !state.rules.fog);

            t.add(box);
        });
    }
}