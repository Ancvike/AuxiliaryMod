package auxiliary.function;

import arc.scene.ui.layout.Table;
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
        if (!state.rules.waves && state.isCampaign()) {
            return new Table(t -> {
            t.name = name;
            t.button(icon, Styles.clearTogglei, this::onClick).size(50f);
        });
        }else return new Table();
    }

    @Override
    public void onClick() {
        isClick = !isClick;
        player.shooting = false;
    }
}