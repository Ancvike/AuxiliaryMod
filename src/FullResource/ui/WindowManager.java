package FullResource.ui;

import arc.scene.ui.layout.Table;
import arc.struct.*;
import mindustry.*;
import mindustry.ui.*;


public class WindowManager {
    public static CoreWindow coreWindow = new CoreWindow();
    public static Table body;

    public static void init() {

        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";

            coreWindow.build();
            t.add(coreWindow).height(coreWindow.getHeight()).width(coreWindow.getWidth());
        });

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