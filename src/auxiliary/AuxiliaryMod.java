package auxiliary;

import arc.Events;
import auxiliary.binding.KeyBind;
import auxiliary.functions.Menu;
import auxiliary.functions.mapEditor.MyMapEditorDialog;
import auxiliary.ui.SettingUI;
import mindustry.game.EventType;
import mindustry.mod.Mod;

import static mindustry.Vars.ui;

public class AuxiliaryMod extends Mod {
    @Override
    public void init() {
        new Menu();
        new KeyBind().init();
        new SettingUI().init();

        Events.on(EventType.ClientLoadEvent.class, e -> ui.editor = new MyMapEditorDialog());
    }
}