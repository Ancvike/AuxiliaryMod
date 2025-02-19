package auxiliary.functions;

import arc.func.Cons;
import mindustry.Vars;
import mindustry.type.Sector;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.state;

public class Launch extends Function {
    PlanetDialog dialog = new PlanetDialog();
    public Launch() {
        super(0, "从此区块发射");

    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
//            state.rules.sector
            dialog.show();
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }

    public void selectSector(Cons<Sector> listener) {

    }
}