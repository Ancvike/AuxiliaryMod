package auxiliary.binding;

import arc.Events;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import auxiliary.function.Function;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.ui.Styles;

public class KeyBind_Mobile extends Function {
    boolean isUnitTrue = false;
    boolean isBuildingTrue = false;
    int count = 0;

    public KeyBind_Mobile() {
        super("mobile-building-repair", Icon.android, "建筑修复");

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            isUnitTrue = Vars.control.input.commandMode;
            if (isUnitTrue && count == 0) {
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "mobile-unit";
                    t.bottom();
                    t.left();
                    t.button(Icon.android, this::unitClick).size(50f).tooltip(tt -> {
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
            isUnitTrue = Vars.control.input.commandMode;
            if (!isUnitTrue && count != 0) {
                count = 0;
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("mobile-unit"));
            }
        });
    }

    @Override
    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.button(icon, Styles.clearTogglei, () -> {

            }).size(50f);
    });
}

public void unitClick() {
    Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
    for (Unit unit : selectedUnits) {
        unit.health = unit.maxHealth;
    }
    Vars.ui.hudfrag.showToast("所选单位已修复");
}
}
