package auxiliary.binding;

import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.input.MobileInput;
import mindustry.ui.Styles;

public class KeyBind_Mobile extends MobileInput {
    boolean isTrue = false;
    int count = 0;

    public void init() {
//        Events.run(EventType.Trigger.uiDrawEnd, () -> Vars.ui.hudGroup.fill(t -> {
//            t.name = "mobile-building";
//            t.bottom();
//            t.right();
//            t.button(Icon.android, this::onClick).size(50f).tooltip(tt -> {
//                tt.setBackground(Styles.black6);
//                tt.label(() -> "建筑修复").pad(2f);
//            }).bottom();
//            t.table().size(300f,50f);
//        }));

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            isTrue = Vars.control.input.commandMode;
            if (isTrue && count == 0) {
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "mobile-unit";
                    t.bottom();
                    t.left();
                    t.button(Icon.android, this::onClick).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> "单位修复").pad(2f);
                    }).left();
                    t.row();
                    t.table().size(48f);
                });
                count++;
            }
        });

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            isTrue = Vars.control.input.commandMode;
            if (!isTrue && count != 0) {
                count = 0;
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("mobile-unit"));
            }
        });
    }

    public void onClick() {
        Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
        for (Unit unit : selectedUnits) {
            unit.health = unit.maxHealth;
        }
        Vars.ui.hudfrag.showToast("所选单位已修复");
    }
}
