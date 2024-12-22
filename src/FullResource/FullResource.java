package FullResource;

import FullResource.core.Core;
import FullResource.ui.WindowManager;
import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class FullResource extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            WindowManager.init();
            Core.init();
        });
    }
}
