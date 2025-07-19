package auxiliary.functions.global;

import arc.Events;
import mindustry.Vars;
import mindustry.content.Planets;
import mindustry.game.EventType;
import mindustry.game.Gamemode;

import static auxiliary.functions.Invincibility.isInvincible;
import static mindustry.Vars.state;

public class GlobalFunction {
    public GlobalFunction() {
        Events.run(EventType.Trigger.update, () -> {
            if (!(Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || !(Vars.state.rules.mode() == Gamemode.sandbox) || !(Vars.state.rules.mode() == Gamemode.editor)) {
                isInvincible = false;
                if (Vars.state.rules.sector != null) {
                    if (Vars.state.rules.sector.planet == Planets.erekir) {
                        state.rules.fog = true;
                    }
                }
            }
        });
    }
}
