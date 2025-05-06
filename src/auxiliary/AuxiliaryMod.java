package auxiliary;

import auxiliary.binding.KeyBind;
import auxiliary.functions.Menu;
import auxiliary.functions.mapEditor.MyMapEditorDialog;
import auxiliary.ui.SettingUI;
import mindustry.mod.Mod;

import static mindustry.Vars.ui;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        new KeyBind().init();
        new SettingUI().init();

        ui.editor = new MyMapEditorDialog();
    }
}