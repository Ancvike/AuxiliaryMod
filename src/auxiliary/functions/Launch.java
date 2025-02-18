package auxiliary.functions;

import mindustry.Vars;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.state;

public class Launch extends Function {
    public PlanetDialog dialog;
    public Launch() {
        super(0, "从此区块发射");
    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            dialog = Vars.ui.planet;
            dialog.show();
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }
}