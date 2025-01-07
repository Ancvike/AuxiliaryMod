//package auxiliary.ui;
//
//import arc.Core;
//import arc.func.Cons;
//import arc.input.KeyCode;
//import arc.scene.event.InputEvent;
//import arc.scene.event.InputListener;
//import arc.scene.ui.Dialog;
//import auxiliary.binding.MyKeyBind;
//import mindustry.Vars;
//import mindustry.graphics.Pal;
//import mindustry.ui.Styles;
//import mindustry.ui.dialogs.BaseDialog;
//import mindustry.ui.dialogs.SettingsMenuDialog;
//
//import static auxiliary.binding.MyKeyBind.*;
//
//public class SettingUI {
//    private static final BaseDialog dialog_setting = new BaseDialog("确认页面");
//    private static final Cons<SettingsMenuDialog.SettingsTable> settingTable = table -> {
//        if (Core.app.isDesktop()) {
//            table.add("传送带升级").left().padRight(40).padLeft(8);
//            table.label(() -> UP.nowKeyCode.value).color(Pal.accent).left().minWidth(90).padRight(20);
//
//            table.button("重新绑定", Styles.defaultt, () -> openDialog(UP)).width(130f);
//
//            table.button("恢复默认", Styles.defaultt, () -> resetKeyBind(UP)).width(130f).pad(2f).padLeft(4f);
//            table.row();
//        } else {
//            dialog_setting.show();
//        }
//    };
//
//    public static void init() {
//        setDialog_setting();
//        Vars.ui.settings.addCategory("AuxiliaryMod设置", settingTable);
//    }
//
//    private static void openDialog(MyKeyBind key) {
//        Dialog rebindDialog = new Dialog("请按一个键…");
//        rebindDialog.titleTable.getCells().first().pad(4);
//        rebindDialog.addListener(new InputListener() {
//            @Override
//            public boolean keyDown(InputEvent event, KeyCode keycode) {
//                rebindDialog.hide();
//                update(key, keycode);
//                return false;
//            }
//        });
//        rebindDialog.show();
//    }
//
//    private static void setDialog_setting() {
//        dialog_setting.cont.add("当前Mod设置只有电脑端的更改键盘键位设置,其他端无需该功能").row();
//        dialog_setting.addCloseButton();
//    }
//}