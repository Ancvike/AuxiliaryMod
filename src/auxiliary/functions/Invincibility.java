package auxiliary.functions;

import arc.Events;
import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.gen.Unit;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class Invincibility extends Function {
    private boolean isInvincible = false;

    Seq<Unit> units = null;
    Unit unitPlayer = null;

    public Invincibility() {
        super(1, "当前单位和所控制单位无敌");

        Events.run(EventType.Trigger.update, () -> {
            if (isInvincible) {
                if (units != null && unitPlayer != null) {
                    if (unitPlayer != Vars.player.unit()) {
                        unitPlayer.health = unitPlayer.maxHealth;
                    }
                    if (units != Vars.control.input.selectedUnits) {
                        for (Unit unit : units) {
                            unit.health = unit.maxHealth;
                        }
                    }
                }
                unitPlayer = Vars.player.unit();
                units = Vars.control.input.selectedUnits;

                player.unit().health = 999999999;
                for (Unit unit : Vars.control.input.selectedUnits) {
                    unit.health = 999999999;
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
                if ((!state.rules.waves && state.isCampaign()) || state.rules.mode() == Gamemode.sandbox) {
                    isInvincible = !isInvincible;
                    if (!isInvincible) {
                        Vars.player.unit().health = Vars.player.unit().maxHealth;
                        for (Unit unit : Vars.control.input.selectedUnits) {
                            unit.health = unit.maxHealth;
                        }
                    }
                } else Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            });

            t.add(box);
        });
    }
}
