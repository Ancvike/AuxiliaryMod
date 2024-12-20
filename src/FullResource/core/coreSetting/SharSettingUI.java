package FullResource.core.coreSetting;

import arc.struct.*;
import FullResource.core.uiFragments.ElementViewFragment;
import FullResource.core.uiFragments.SidebarSwitcher;
import FullResource.core.uiFragments.sidebarWindows.WindowManager;

import static arc.Core.scene;
import static FullResource.core.coreSetting.SettingHelper.*;
import static mindustry.Vars.*;

public class SharSettingUI {
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
        Seq<SharSetting> tapSeq = new Seq<>();
        addGraphicCheckSetting("sidebar", !mobile, tapSeq, () -> sidebarSwitcherFragment.rebuildSidebarTable());

    }
}
