package auxiliary;

import auxiliary.binding.KeyBind;
import auxiliary.functions.Menu;
import auxiliary.ui.SettingUI;
import mindustry.mod.Mod;


public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        new KeyBind().init();
        new SettingUI().init();
    }
}