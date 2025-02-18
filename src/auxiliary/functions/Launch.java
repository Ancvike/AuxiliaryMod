package auxiliary.functions;

import arc.func.Cons;
import mindustry.Vars;
import mindustry.type.Sector;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.control;
import static mindustry.Vars.state;
import static mindustry.ui.dialogs.PlanetDialog.Mode.look;

public class Launch extends Function {
    public MyPlanetDialog dialog;

    public Launch() {
        super(0, "从此区块发射");
    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            dialog = (MyPlanetDialog) Vars.ui.planet;
            dialog.showSelect(state.rules.sector, other -> {
                if (other.planet == state.rules.sector.planet) {
                    control.playSector(other);
                }
            });
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }
}

class MyPlanetDialog extends PlanetDialog {
    @Override
    public void showSelect(Sector sector, Cons<Sector> listener) {

    }
}