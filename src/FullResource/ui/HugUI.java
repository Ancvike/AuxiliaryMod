package FullResource.ui;

import FullResource.core.Core;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

public class HugUI {
    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.button(Icon.upload, Styles.emptyi, Core::onClick).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "full").pad(2f);
            });
            t.top();
            t.x = 300;
        });
    }
}
