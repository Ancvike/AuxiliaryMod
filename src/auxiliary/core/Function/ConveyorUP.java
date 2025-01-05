package auxiliary.core.Function;

import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import mindustry.Vars;
import mindustry.ui.dialogs.BaseDialog;

public class ConveyorUP {

    public static void init() {
        Vars.ui.hudGroup.fill(t -> {
            t.addListener(new InputListener() {
                @Override
                public boolean keyDown(InputEvent event, KeyCode keycode) {
                    if (keycode == KeyCode.u) {
                        load();
                    }
                    return false;
                }
            });
        });
    }
    public static void load () {
        BaseDialog dialog = new BaseDialog("");
        dialog.addCloseButton();
        dialog.show();
    }
}