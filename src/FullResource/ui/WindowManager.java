package FullResource.ui;

import arc.scene.ui.layout.Table;
import arc.struct.*;
import mindustry.*;
import mindustry.ui.*;

import static FullResource.core.Core.dialog;

public class WindowManager {
    public static final Seq<Window> windows = new Seq<>();
    public static Table body;

    public static void init() {
        windows.addAll(new CoreWindow());

        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";
            for (Window window : windows) {
                window.build();
                dialog.add("CoreWindow.build()触发").row();
                t.add(window).height(window.getHeight()).width(window.getWidth());
            }
        });

        body = new Table(t -> {
            t.name = "Window Buttons";
            t.left();

            for (Window window : windows) {
                t.button(window.icon, Styles.emptyi, () -> {
                    window.parent.setLayoutEnabled(false);
                    window.toggle();
                    for (Window w : windows) {
                        w.setLayoutEnabled(true);
                    }
                }).size(40f).tooltip(tt -> {
                    tt.setBackground(Styles.black6);
                    tt.label(() -> "66666").pad(2f);
                });
                t.row();
            }
        });

        Vars.ui.hudGroup.fill(t -> {
            t.add(body);
            t.top();
            t.x = 300;
        });
    }
}