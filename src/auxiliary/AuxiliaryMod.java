package auxiliary;

import auxiliary.binding.HPChange_KeyBind;
import auxiliary.block.MyBlocks;
import auxiliary.functions.Menu;
import auxiliary.settings.SettingUI;
import mindustry.mod.Mod;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        new HPChange_KeyBind();
        new SettingUI().init();
    }

    @Override
    public void loadContent() {
        new MyBlocks().load();
    }
}