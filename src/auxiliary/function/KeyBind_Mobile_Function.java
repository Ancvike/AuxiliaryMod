package auxiliary.function;

import arc.Events;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class KeyBind_Mobile_Function extends Function {
    public static boolean isClick = false;
    public static boolean isShow = false;

    public KeyBind_Mobile_Function() {
        super("mobile-building-repair", Icon.android, "建筑修复");
        Events.run(EventType.Trigger.draw, () -> isShow = !state.rules.waves && state.isCampaign());
    }

    @Override
    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.button(icon, Styles.clearTogglei, this::onClick).size(50f).visible(isShow);
        });
    }

    @Override
    public void onClick() {
        isClick = !isClick;
        player.shooting = false;
    }
}