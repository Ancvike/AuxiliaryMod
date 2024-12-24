package FullResource.core;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.ui.Styles;

import static FullResource.ui.WindowManager.coreWindow;

public class Core {
    public static Table body;

    public Core() {
        body = new Table(t -> {
            t.name = "Window Buttons";
            t.left();

            t.button(coreWindow.icon, Styles.emptyi, () -> {
                coreWindow.parent.setLayoutEnabled(false);
                coreWindow.toggle();

                coreWindow.setLayoutEnabled(true);

            }).size(40f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "66666").pad(2f);
            });
            t.row();
        });

        Vars.ui.hudGroup.fill(t -> {
            t.add(body);
            t.top();
            t.x = 300;
        });
    }
}