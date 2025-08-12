package auxiliary.functions.global;

import arc.Events;
import mindustry.Vars;
import mindustry.game.EventType;

import static auxiliary.functions.NoLimitUnitAmount.isOpen;

public class GlobalFunction {
    public GlobalFunction() {

        Events.run(EventType.Trigger.update, () -> isOpen = Vars.state.rules.unitCap == 1000);
    }
}
