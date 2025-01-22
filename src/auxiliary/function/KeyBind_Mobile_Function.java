package auxiliary.function;

import arc.Events;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class KeyBind_Mobile_Function extends Function {
    public static boolean isClick = false;
    ImageButton button = new ImageButton(icon, Styles.clearTogglei);

    public KeyBind_Mobile_Function() {
        super("mobile-building-repair", Icon.android, "建筑修复");
        Events.run(EventType.Trigger.draw, () -> {
            if (!state.rules.waves && state.isCampaign()) {
                button.visible = true;
                button.clicked(this::onClick);
            }else {
                button.visible = false;
            }
        });
    }

    @Override
    public Table setTable() {
        return new Table(t -> {
            t.name = name;
            t.add(button).size(50f);
        });
    }

    @Override
    public void onClick() {
        isClick = !isClick;
        player.shooting = false;
    }
}