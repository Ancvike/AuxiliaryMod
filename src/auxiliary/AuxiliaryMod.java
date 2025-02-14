package auxiliary;

import auxiliary.binding.KeyBind;
import auxiliary.functions.Menu;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        KeyBind.init();
    }
}