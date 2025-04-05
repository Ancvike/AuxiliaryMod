package auxiliary.functions;

import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;

import static mindustry.Vars.enableLight;

public class SunLight extends Function {

    public SunLight() {
        super(0, "太阳光阴影");
    }

    @Override
    public Table function() {
        return new Table(t -> {
            Slider slider = new Slider(0, 1, 1, false);
            slider.setValue(enableLight ? 0 : 1);
            slider.moved(value -> {
                if (value == 0) enableLight = true;
                else if (value == 1) enableLight = false;
            });

            t.add("[green]开").margin(0f).pad(0f);
            t.add(slider);
            t.add("[red]关").margin(0f).pad(0f);
        });
    }
}
