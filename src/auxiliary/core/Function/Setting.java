package auxiliary.core.Function;

import arc.KeyBinds;
import arc.func.Cons;
import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.Dialog;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;
import mindustry.ui.dialogs.SettingsMenuDialog;

import static auxiliary.core.binding.Binding.UP;

public class Setting extends Dialog {
    public static Cons<SettingsMenuDialog.SettingsTable> settingTable;
    private static String conveyorUP_KeyBind = "U";
    protected static Dialog rebindDialog;
    protected static KeyBinds.KeyBind rebindKey = null;

    public static void init() {
        settingTable = settingTable -> {
            settingTable.add("11111111111").left().padRight(40).padLeft(8);
            settingTable.label(() -> conveyorUP_KeyBind).color(Pal.accent).left().minWidth(90).padRight(20);

            settingTable.button("重新绑定", Styles.defaultt, () -> openDialog(UP)).width(130f);
            settingTable.button("恢复默认", Styles.defaultt, Setting::setDefault_KeyBind).width(130f).pad(2f).padLeft(4f);
            settingTable.row();
        };
    }

    public static void setDefault_KeyBind() {
        conveyorUP_KeyBind = "U";
    }

    private static void openDialog(KeyBinds.KeyBind name) {
        rebindKey = name;
        rebindDialog = new Dialog("请按一个键…");
        rebindDialog.titleTable.getCells().first().pad(4);
        rebindDialog.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, KeyCode keycode) {
                rebindDialog.hide();
//                rebind(section, name, keycode);
                BaseDialog baseDialog = new BaseDialog("");
                baseDialog.cont.add("已绑定按键: " + keycode).color(Pal.accent);
                baseDialog.addCloseButton();
                baseDialog.show();
                return false;
            }
        });
        rebindDialog.show();
    }

//    void rebind(KeyBinds.Section section, KeyBinds.KeyBind bind, KeyCode newKey){
//        if(rebindKey == null) return;
//        rebindDialog.hide();
//        boolean isAxis = bind.defaultValue(section.device.type()) instanceof KeyBinds.Axis;
//
//        if(isAxis){
//            if(newKey.axis || !rebindMin){
//                section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, newKey.axis ? new KeyBinds.Axis(newKey) : new KeyBinds.Axis(minKey, newKey));
//            }
//        }else{
//            section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, new KeyBinds.Axis(newKey));
//        }
//
//        if(rebindAxis && rebindMin && !newKey.axis){
//            rebindMin = false;
//            minKey = newKey;
//            openDialog(section, rebindKey);
//        }else{
//            rebindKey = null;
//            rebindAxis = false;
//        }
//    }
}
