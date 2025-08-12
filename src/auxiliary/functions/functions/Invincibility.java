package auxiliary.functions.functions;

import arc.Events;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;

import static mindustry.Vars.player;

public class Invincibility extends Function {
    public static boolean isInvincible = false;

    public static Seq<Unit> invincibleUnits = null;
    public static Unit unitPlayer = null;

    public Invincibility() {
        super(0, new Table(table -> table.add("当前单位和所控制单位无敌")));

        Events.run(EventType.Trigger.update, () -> {
            if (isInvincible) {
                if (invincibleUnits != null && unitPlayer != null) {
                    unitPlayer.health = unitPlayer.maxHealth;

                    for (Unit unit : invincibleUnits) {
                        unit.health = unit.maxHealth;
                    }
                }
                unitPlayer = Vars.player.unit();
                invincibleUnits = Vars.control.input.selectedUnits;

                player.unit().health = 99999999;
                for (Unit unit : Vars.control.input.selectedUnits) {
                    unit.health = 99999999;
                }
            }
        });
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(isInvincible));
            box.changed(() -> {
                isInvincible = !isInvincible;
                if (!isInvincible) {
                    Vars.player.unit().health = Vars.player.unit().maxHealth;
                    for (Unit unit : Vars.control.input.selectedUnits) {
                        unit.health = unit.maxHealth;
                    }
                    if (invincibleUnits != null && unitPlayer != null) {
                        unitPlayer.health = unitPlayer.maxHealth;

                        for (Unit unit : invincibleUnits) {
                            unit.health = unit.maxHealth;
                        }
                    }
                }
            });

            t.add(box);
        });
    }
}