package auxiliary.function.mobile_special;

import auxiliary.function.Function;
import mindustry.Vars;
import mindustry.gen.Icon;

import static mindustry.Vars.state;

public class RemoveFog_Mobile extends Function {

    public RemoveFog_Mobile() {
        super("remove-fog-mobile", Icon.link, "迷雾开关");
    }

    @Override
    public void onClick() {
        state.rules.fog = !state.rules.fog;
        if (state.rules.fog) {
            Vars.ui.hudfrag.showToast("迷雾显示已开启");
        } else {
            Vars.ui.hudfrag.showToast("迷雾显示已关闭");
        }
    }
}
