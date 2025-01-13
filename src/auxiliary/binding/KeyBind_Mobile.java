package auxiliary.binding;

import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.Styles;


public class KeyBind_Mobile {
    public void init() {
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.control.input.commandMode) {
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "mobile-recovery-unit";
                    t.button(Icon.android, this::onClick).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> "单位修复").pad(2f);
                    });
                    t.left();
                    t.bottom();
                });
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
