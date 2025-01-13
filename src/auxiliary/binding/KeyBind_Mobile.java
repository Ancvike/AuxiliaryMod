package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.Styles;


public class KeyBind_Mobile {
    public void init() {
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (Vars.control.input.commandMode) {
                Vars.control.input.uiGroup.fill(t -> {
                    t.name = "mobile-recovery-unit";
                    t.button(Icon.android, this::onClick).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> "单位修复").pad(2f);
                    });
                });
            } else {
                Vars.control.input.uiGroup.removeChild(Vars.ui.hudGroup.find("mobile-recovery-unit"));
            }
        });
    }

    private void onClick() {
        Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
        for (Unit unit : selectedUnits) {
            unit.health = unit.maxHealth;
        }
        Vars.ui.hudfrag.showToast("所选单位已修复");
    }
}
