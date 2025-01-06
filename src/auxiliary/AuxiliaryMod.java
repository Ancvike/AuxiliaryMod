package auxiliary;

import auxiliary.dialogs.Dialogs;
import arc.Events;
import auxiliary.function.BuildingRestoration;
import auxiliary.function.FullResource;
import auxiliary.ui.UIManager;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            Dialogs.init();
            UIManager.init();
            FullResource.init();
            BuildingRestoration.init();
        });
    }
}
