package auxiliary.core.binding;

import arc.KeyBinds;
import arc.input.InputDevice;
import arc.input.KeyCode;

public enum Binding implements KeyBinds.KeyBind {
;

    private final KeyBinds.KeybindValue defaultValue;
    private final String category;

    Binding(KeyBinds.KeybindValue defaultValue, String category) {
        this.defaultValue = defaultValue;
        this.category = category;
    }

    Binding(KeyBinds.KeybindValue defaultValue) {
        this(defaultValue, null);
    }

    @Override
    public KeyBinds.KeybindValue defaultValue(InputDevice.DeviceType type) {
        return defaultValue;
    }

    @Override
    public String category() {
        return category;
    }
}
