package auxiliary.functions;

import arc.scene.ui.Slider;

import static mindustry.Vars.enableLight;

public class Light extends Function {

    public Light() {
        super(1, "地图光照");

        slider = new Slider(0, 50, 50, false);
        slider.setValue(enableLight ? 0 : 50);
        slider.moved(moved -> {
            if (moved == 0) enableLight = true;
            else if (moved == 50) enableLight = false;
        });
    }
}
