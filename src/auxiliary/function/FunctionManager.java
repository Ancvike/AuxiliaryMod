package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.input.KeyCode;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.mobile;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();
    private static float time;

    public static void init() {
        functions.addAll(new FullResource(), new BuildingRestoration());

        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            for (Function function : functions) {
                t.add(function.setTable()).size(50f).tooltip(tt -> {
                    tt.setBackground(Styles.black6);
                    tt.label(() -> function.labelName).pad(2f);
                });
                if (!(mobile && Core.settings.getBool("landscape"))) t.row();
            }
            if (mobile && Core.settings.getBool("landscape")) {
                t.bottom();
            } else {
                t.right();
            }
        });
        check();
    }

    public static void check() {
        if (!mobile) {
            Events.on(EventType.WorldLoadEvent.class, e -> Vars.ui.hudGroup.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    BaseDialog dialog = new BaseDialog("");
                    dialog.addCloseButton();
                    dialog.show();
                    return false;
                }
            }));
        }
    }
}

