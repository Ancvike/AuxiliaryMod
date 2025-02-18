package auxiliary.functions;

import mindustry.Vars;
import mindustry.ctype.UnlockableContent;

import static mindustry.Vars.control;
import static mindustry.Vars.state;

public class Launch extends Function {

    public Launch() {
        super(0, "从此区块发射");
    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            Vars.ui.planet.showSelect(state.rules.sector, other -> {
                if (other.planet == state.rules.sector.planet) {
                    other.planet.unlockedOnLand.each(UnlockableContent::unlock);
                    control.playSector(other);
                }
            });
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }
}
