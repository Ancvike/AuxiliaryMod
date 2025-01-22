package auxiliary.function;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class KeyBind_Mobile_Function extends Function {
    public static boolean isClick = false;

    public KeyBind_Mobile_Function() {
        super("mobile-building-repair", Icon.android, "建筑修复");
    }

    @Override
    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.button(icon, Styles.clearTogglei, this::onClick).size(50f);
        });
    }

    @Override
    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
            isClick = !isClick;
            player.shooting = false;
        } else {
            Vars.ui.hudfrag.showToast("区块未占领,无法使用该功能");
        }
    }
}
