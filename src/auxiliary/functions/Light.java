package auxiliary.functions;

import arc.scene.ui.Slider;

import static mindustry.Vars.enableLight;
import static mindustry.Vars.state;

public class Light extends Function {

    public Light() {
        super(1, "地图光照");

        slider = new Slider(0, 50, 50, false);
        slider.setValue(state.rules.fog ? 0 : 50);
        slider.moved(moved -> {
            if (moved == 0) enableLight = true;
            else if (moved == 50) enableLight = false;
        });
    }
}
