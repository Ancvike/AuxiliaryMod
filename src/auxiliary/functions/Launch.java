package auxiliary.functions;

import mindustry.Vars;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.*;

public class Launch extends Function {
    MyPlanetDialog dialog = new MyPlanetDialog();

    public Launch() {
        super(0, "从此区块发射");

    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            PlanetDialog.debugSelect = true;
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }


}

class MyPlanetDialog extends PlanetDialog {
}