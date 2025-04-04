package auxiliary.functions;

import arc.scene.ui.Slider;

import static mindustry.Vars.state;

public class WarfareFog extends Function {

    public WarfareFog() {
        super(0, 999, "战争迷雾");

        Slider slider = new Slider(0, 50, 50, false);
        slider.setValue(state.rules.fog ? 0 : 50);
        slider.moved(moved -> {
            if (moved == 0) state.rules.fog = true;
            else if (moved == 50) state.rules.fog = false;
        });
    }
}