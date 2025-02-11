package auxiliary;

import auxiliary.functions.Menu;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
    }
}