package FullResource;

import FullResource.core.Core;
import FullResource.core.coreSetting.SharSettingUI;
import FullResource.core.uiFragments.sidebarWindows.WindowManager;
import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class FullResource extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            new Core();
            SharSettingUI.init();
            WindowManager.init();
        });
    }
}
