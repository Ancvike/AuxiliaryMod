package collection;

import collection.core.BuildingRestoration;
import collection.ui.HugUI;
import collection.ui.Dialogs;
import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class Collection extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            HugUI.init();
            Dialogs.init();
            BuildingRestoration.init();
        });
    }
}
