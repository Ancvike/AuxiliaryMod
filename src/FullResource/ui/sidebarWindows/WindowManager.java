package FullResource.ui.sidebarWindows;

import arc.scene.ui.layout.Table;
import arc.struct.*;
import mindustry.*;
import mindustry.ui.*;

public class WindowManager {
    public static final Seq<Window> windows = new Seq<>();
    public static Table body;

    public static void init(){
        windows.addAll(
            new CoreWindow()
        );

        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";
            for(Window window : windows) {
                window.build();
                t.add(window).height(window.getHeight()).width(window.getWidth());
            }
        });

        body = new Table(t -> {
            t.name = "Window Buttons";
            t.left();

            for(Window window : windows){
                t.button(window.icon, Styles.emptyi, () -> {
                    window.parent.setLayoutEnabled(false);
                    window.toggle();
                    for (Window w : windows) {
                        w.setLayoutEnabled(true);
                    }
                }).size(40f).tooltip(tt -> {
                    tt.setBackground(Styles.black6);
                    tt.label(() -> "check").pad(2f);
                });
                t.row();
            }
        }).left();
    }
}