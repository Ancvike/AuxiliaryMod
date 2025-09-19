package auxiliary;

import auxiliary.block.MyBlocks;
import auxiliary.functions.Menu;
import auxiliary.settings.SettingUI;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();

        new SettingUI().init();
    }

    @Override
    public void loadContent() {
        new MyBlocks().load();
    }
}