package auxiliary.ui;

import arc.Core;
import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.Dialog;
import arc.struct.OrderedMap;
import arc.util.Time;
import mindustry.Vars;
import mindustry.graphics.Pal;
import mindustry.ui.Styles;

import static arc.Core.bundle;
import static arc.Core.keybinds;

public class SettingUI extends Dialog{
    protected KeyBinds.KeyBind rebindKey = null;
    protected boolean rebindAxis = false;
    protected boolean rebindMin = true;
    protected KeyCode minKey = null;
    protected Dialog rebindDialog;

    public void init() {
        KeyBinds.Section[] sections = Core.keybinds.getSections();
        var tstyle = Styles.defaultt;
        Vars.ui.settings.addCategory("AuxiliaryMod设置", table -> {
            for (KeyBinds.Section section : sections) {
                for (KeyBinds.KeyBind keybind : keybinds.getKeybinds()) {
                    table.add("111111").left().padRight(40).padLeft(8);
                    table.label(() -> keybinds.get(section, keybind).key.toString()).color(Pal.accent).left().minWidth(90).padRight(20);

                    table.button("重新绑定", tstyle, () -> {
                        rebindAxis = false;
                        rebindMin = false;
                        openDialog(section, keybind);
                    }).width(130f);
                    table.button("恢复默认", tstyle, () -> keybinds.resetToDefault(section, keybind)).width(130f).pad(2f).padLeft(4f);
                    table.row();
                }
            }
        });
    }

    void rebind(KeyBinds.Section section, KeyBinds.KeyBind bind, KeyCode newKey) {
        if (rebindKey == null) return;
        rebindDialog.hide();
        boolean isAxis = bind.defaultValue(section.device.type()) instanceof KeyBinds.Axis;

        if (isAxis) {
            if (newKey.axis || !rebindMin) {
                section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, newKey.axis ? new KeyBinds.Axis(newKey) : new KeyBinds.Axis(minKey, newKey));
            }
        } else {
            section.binds.get(section.device.type(), OrderedMap::new).put(rebindKey, new KeyBinds.Axis(newKey));
        }

        if (rebindAxis && isAxis && rebindMin && !newKey.axis) {
            rebindMin = false;
            minKey = newKey;
            openDialog(section, rebindKey);
        } else {
            rebindKey = null;
            rebindAxis = false;
        }
    }

    private void openDialog(KeyBinds.Section section, KeyBinds.KeyBind name) {
        rebindDialog = new Dialog(rebindAxis ? bundle.get("keybind.press.axis") : bundle.get("keybind.press"));

        rebindKey = name;

        rebindDialog.titleTable.getCells().first().pad(4);

        if (section.device.type() == InputDevice.DeviceType.keyboard) {

            rebindDialog.addListener(new InputListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    if (Core.app.isAndroid()) return false;
                    rebind(section, name, button);
                    return false;
                }

                @Override
                public boolean keyDown(InputEvent event, KeyCode keycode) {
                    rebindDialog.hide();
                    rebind(section, name, keycode);
                    return false;
                }

                @Override
                public boolean scrolled(InputEvent event, float x, float y, float amountX, float amountY) {
                    if (!rebindAxis) return false;
                    rebindDialog.hide();
                    rebind(section, name, KeyCode.scroll);
                    return false;
                }
            });
        }

        rebindDialog.show();
        Time.runTask(1f, () -> getScene().setScrollFocus(rebindDialog));
    }
}