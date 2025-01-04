package auxiliary.ui;

import arc.KeyBinds;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.util.Strings;
import mindustry.Vars;
import mindustry.graphics.Pal;

import static arc.Core.*;
import static arc.Core.keybinds;

public class SettingUI {
    public static void init() {
        Vars.ui.settings.addCategory("AuxiliaryMod设置", table -> input.keyTap(KeyCode.u));
//        for(KeyBinds.KeyBind keybind : keybinds.getKeybinds()){
//            if(keybind.defaultValue(section.device.type()) instanceof KeyBinds.Axis){
//                table.add(bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), Color.white).left().padRight(40).padLeft(8);
//
//                table.labelWrap(() -> {
//                    KeyBinds.Axis axis = keybinds.get(section, keybind);
//                    return axis.key != null ? axis.key.toString() : axis.min + " [red]/[] " + axis.max;
//                }).color(Pal.accent).left().minWidth(90).fillX().padRight(20);
//
//                table.button("@settings.rebind", tstyle, () -> {
//                    rebindAxis = true;
//                    rebindMin = true;
//                    openDialog(section, keybind);
//                }).width(130f);
//            }else{
//                table.add(bundle.get("keybind." + keybind.name() + ".name", Strings.capitalize(keybind.name())), Color.white).left().padRight(40).padLeft(8);
//                table.label(() -> keybinds.get(section, keybind).key.toString()).color(Pal.accent).left().minWidth(90).padRight(20);
//
//                table.button("@settings.rebind", tstyle, () -> {
//                    rebindAxis = false;
//                    rebindMin = false;
//                    openDialog(section, keybind);
//                }).width(130f);
//            }
//            table.button("@settings.resetKey", tstyle, () -> keybinds.resetToDefault(section, keybind)).width(130f).pad(2f).padLeft(4f);
//            table.row();
//        }
    }
}
