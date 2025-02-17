package auxiliary.functions;

import mindustry.Vars;

import static mindustry.Vars.state;

public class Launch extends Function {

    public Launch() {
        super(0, "从此区块发射");
    }

    @Override
    public void onClick() {
        if (state.isCampaign()) {
            Vars.ui.hudfrag.showToast("111");
        }else {
            Vars.ui.hudfrag.showToast("当前功能仅在战役中使用");
        }
    }
}
