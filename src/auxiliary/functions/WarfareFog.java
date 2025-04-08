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
//            Slider slider = new Slider(0, 1, 1, false);
//            slider.setValue(state.rules.fog ? 0 : 1);
//            slider.moved(value -> {
//                if (value == 0) state.rules.fog = true;
//                else if (value == 1) state.rules.fog = false;
//            });
//
//            t.add("[green]开");
//            t.add(slider);
//            t.add("[red]关");
            CheckBox box = new CheckBox("");
            box.changed(() -> state.rules.fog = !state.rules.fog);
            box.update(() -> box.setChecked(state.rules.fog));
            t.add(box).right();
        });
    }
}