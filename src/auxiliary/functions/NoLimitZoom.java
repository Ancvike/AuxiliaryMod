package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import mindustry.Vars;

public class NoLimitZoom extends Function {

    public NoLimitZoom() {
        super(0, "无限制缩放");
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(Vars.renderer.minZoom != 1.5f));
            box.changed(() -> Vars.renderer.minZoom = Vars.renderer.minZoom == 1.5f ? 0.15f : 1.5f);

            t.add(box);
        });
    }
}
