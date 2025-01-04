package auxiliary;

import auxiliary.core.Function.FunctionManager;
import auxiliary.core.binding.BindingManager;
import auxiliary.ui.Dialogs;
import arc.Events;
import auxiliary.ui.UIManager;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            Dialogs.init();
            UIManager.init();
            BindingManager.init();
            FunctionManager.init();
        });
    }
}
