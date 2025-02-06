package auxiliary;

import auxiliary.binding.KeyBind;
import arc.Events;
import auxiliary.function.FunctionManager;
import auxiliary.function.Menu;
import auxiliary.ui.SettingUI;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            SettingUI.init();
            KeyBind.init();
            FunctionManager.init();
            new Menu();
        });
    }
}
