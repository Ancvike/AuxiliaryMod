package auxiliary;

import auxiliary.binding.KeyBind_Keyboard;
import auxiliary.dialogs.Dialogs;
import arc.Events;
import auxiliary.function.FunctionManager;
import auxiliary.ui.SettingUI;
import mindustry.game.EventType;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    public void init() {
        Events.on(EventType.ClientLoadEvent.class, e -> {
            Dialogs.init();
            SettingUI.init();
            new KeyBind_Keyboard().init();
            FunctionManager.init();
        });
    }
}
