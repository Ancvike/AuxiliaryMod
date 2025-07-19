package auxiliary;

import auxiliary.binding.HPChange_KeyBind;
import auxiliary.binding.HPChange_Mobile_KeyBind;
import auxiliary.block.MyBlocks;
import auxiliary.functions.Menu;
import auxiliary.functions.global.GlobalFunction;
import auxiliary.ui.SettingUI;
import mindustry.mod.Mod;

import static mindustry.Vars.mobile;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        if (mobile) new HPChange_Mobile_KeyBind();
        else new HPChange_KeyBind();
        new SettingUI().init();
        new GlobalFunction();
    }

    @Override
    public void loadContent() {
        new MyBlocks().load();
    }
}