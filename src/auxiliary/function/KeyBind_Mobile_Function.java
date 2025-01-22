package auxiliary.function;

import arc.scene.ui.layout.Table;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.player;

public class KeyBind_Mobile_Function extends Function {
    public static boolean isClick = false;

    public KeyBind_Mobile_Function() {
        super("mobile-building-repair", Icon.android, "建筑修复");
    }

    @Override
    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.button(icon, Styles.clearTogglei, () -> {
                isClick = !isClick;
                player.shooting = false;
            }).size(50f);
        });
    }
}
