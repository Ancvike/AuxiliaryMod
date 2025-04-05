package auxiliary.functions;

import arc.Events;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.player;

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
        Events.run(EventType.Trigger.update, () -> {
            if (!isInvincible) {
                Vars.player.unit().health = Vars.player.unit().maxHealth;
                for (Unit unit : Vars.control.input.selectedUnits) {
                    unit.health = unit.maxHealth;
                }
            }
        });

        Vars.ui.hudGroup.fill(t -> {
            BaseDialog baseDialog = new BaseDialog("按键设置");
            baseDialog.addCloseButton();
            t.button("00", () -> {
                baseDialog.cont.add("" + Vars.player.unit().health);
                baseDialog.show();
            });
        });
//调试代码
    }

    @Override
    public Table function() {
        return new Table(t -> {
            Slider slider = new Slider(0, 1, 1, false);
            slider.setValue(isInvincible ? 0 : 1);
            slider.moved(value -> {
                if (value == 0) isInvincible = true;
                else if (value == 1) isInvincible = false;
            });

            t.add("[green]开");
            t.add(slider);
            t.add("[red]关");
        });
    }
}
