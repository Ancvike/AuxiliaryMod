package auxiliary.core.Function;

import arc.Core;
import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.input.Binding;
import mindustry.ui.dialogs.BaseDialog;

public class ConveyorUP {

    public static void init() {
        Events.on(EventType.WorldLoadEvent.class, e -> {
            if (!Core.app.isDesktop()) return;
            if (Vars.state.isGame()) {
                if (!Core.scene.hasKeyboard()) {
                    if (Core.input.keyDown(Binding.schematic_select)) {
                        BaseDialog dialog = new BaseDialog("");
                        dialog.addCloseButton();
                        dialog.show();
                    }
                }
            }
        });
    }
}
