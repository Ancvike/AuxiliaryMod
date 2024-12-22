package FullResource.ui;

import mindustry.Vars;

public class WindowManager {
    static CoreWindow coreWindow = new CoreWindow();

    public static void init() {

        // windows place for dragging
        Vars.ui.hudGroup.fill(t -> {
            t.name = "Windows";
            coreWindow.build();
            t.add(coreWindow).height(coreWindow.getHeight()).width(coreWindow.getWidth());
        });
    }
}