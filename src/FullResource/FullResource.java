package FullResource;

import FullResource.core.Core;
import FullResource.ui.coreSetting.SharSettingUI;
import FullResource.ui.uiFragments.FragmentManager;
import FullResource.ui.sidebarWindows.WindowManager;
import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class FullResource extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            new Core();
            SharSettingUI.init();
            WindowManager.init();
            FragmentManager.init();
        });
    }
}