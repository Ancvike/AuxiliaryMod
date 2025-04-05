package auxiliary.functions;

import arc.Events;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.player;

public class Invincibility extends Function {
    private boolean isInvincible = false;

    public Invincibility() {
        super(1, "当前单位和控制单位无敌");

        Events.run(EventType.Trigger.update, () -> {
            if (isInvincible) {
                //Vars.control.input.selectedUnits
            }
        });
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

            t.add("[green]开").margin(0f).pad(0f);
            t.add(slider);
            t.add("[red]关").margin(0f).pad(0f);
        });
    }
}
