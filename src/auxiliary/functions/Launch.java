package auxiliary.functions;

import arc.func.Cons;
import mindustry.Vars;
import mindustry.type.*;
import mindustry.ui.dialogs.PlanetDialog;

import static mindustry.Vars.*;
import static mindustry.ui.dialogs.PlanetDialog.Mode.*;

public class Launch extends Function {
    MyPlanetDialog dialog = new MyPlanetDialog();

    public Launch() {
        super(0, "从此区块发射");
    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            dialog.showSelect(state.rules.sector, sector -> {

            });
        } else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }


}

class MyPlanetDialog extends PlanetDialog {
    @Override
    public void showSelect(Sector sector, Cons<Sector> listener) {
        selected = null;
        hovered = null;
        launching = false;
        this.listener = listener;

        zoom = 1f;
        state.zoom = 1f;
        state.uiAlpha = 0f;
        state.otherCamPos = null;
        launchSector = sector;

        mode = select;
    }
}