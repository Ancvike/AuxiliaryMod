package auxiliary.function;

import arc.Events;
import arc.input.KeyCode;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.ImageButton;
import arc.scene.ui.layout.Table;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.ui.Styles;

import static mindustry.Vars.player;
import static mindustry.Vars.state;

public class KeyBind_Mobile_Function extends Function {
    public static boolean isOpen = false;
    public static boolean isClick = false;
    ImageButton button = new ImageButton(icon, Styles.clearTogglei);

    public KeyBind_Mobile_Function() {
        super("mobile-building-repair", Icon.android, "建筑修复");

        button.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                isClick = !isClick;
                return false;
            }

            @Override
            public void touchUp(InputEvent event, float x, float y, int pointer, KeyCode button) {
                isClick = !isClick;
            }

        });

        Events.run(EventType.Trigger.draw, () -> {
            if (!state.rules.waves && state.isCampaign()) {
                button.visible = true;
                button.clicked(this::onClick);
            } else {
                isOpen = false;
                isClick = false;
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
        isOpen = !isOpen;
        player.shooting = false;
    }
}