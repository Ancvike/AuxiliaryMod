package FullResource.ui.uiFragments;


import FullResource.ui.sidebarWindows.WindowManager;

import static arc.Core.scene;
import static mindustry.Vars.ui;

public class FragmentManager {
    public static ElementViewFragment elementViewFragment;
    public static SidebarSwitcher sidebarSwitcherFragment;

    public static void init() {
        elementViewFragment = new ElementViewFragment(
                scene.root,
                ui.picker, ui.editor, ui.controls, ui.restart, ui.join, ui.discord,
                ui.load, ui.custom, ui.language, ui.database, ui.settings, ui.host,
                ui.paused, ui.about, ui.bans, ui.admins, ui.traces, ui.maps, ui.content,
                ui.planet, ui.research, ui.mods, ui.schematics, ui.logic
        );

        sidebarSwitcherFragment = new SidebarSwitcher(
                WindowManager.body
        );
    }
}