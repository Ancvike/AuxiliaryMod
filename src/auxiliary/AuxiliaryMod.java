package auxiliary;

import auxiliary.core.BuildingRestoration;
import auxiliary.ui.HugUI;
import auxiliary.ui.Dialogs;
import arc.Events;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            HugUI.init();
            Dialogs.init();
            BuildingRestoration.init();
        });
    }
}
