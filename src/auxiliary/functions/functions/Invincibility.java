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
    static boolean isInvincible = false;

    static Seq<Unit> invincibleUnits = new Seq<>();
    static Unit unitPlayer = null;

    public Invincibility() {
        super(0, new Table(table -> table.add("当前单位和所控制单位无敌").tooltip("[red]建议关闭后保存, 否则会导致单位血量==999999")));

        Events.run(EventType.Trigger.update, () -> {
            if (isInvincible) {
                if (invincibleUnits.size > 0 && unitPlayer != null) {
                    for (Unit unit : invincibleUnits) {
                        unit.health = unit.maxHealth;
                    }
                    unitPlayer.health = unitPlayer.maxHealth;
                    invincibleUnits.clear();
                    unitPlayer = null;
                }

                for (Unit unit : Vars.control.input.selectedUnits) {
                    invincibleUnits.add(unit);
                    unit.health = 999999;
                }
                unitPlayer = player.unit();
                player.unit().health = 999999;
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
                    if (invincibleUnits.size > 0 && unitPlayer != null) {
                        for (Unit unit : invincibleUnits) {
                            unit.health = unit.maxHealth;
                        }
                        unitPlayer.health = unitPlayer.maxHealth;
                        invincibleUnits.clear();
                        unitPlayer = null;
                    }

                    for (Unit unit : Vars.control.input.selectedUnits) {
                        unit.health = unit.maxHealth;
                    }
                    player.unit().health = player.unit().maxHealth;
                }
            });
            t.add(box);
        });
    }
}