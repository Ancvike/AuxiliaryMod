package auxiliary.function;

import arc.Core;
import arc.input.KeyCode;
import arc.scene.event.ClickListener;
import arc.scene.event.InputEvent;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.ui.Styles;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();

    public static void init() {
        functions.addAll(new FullResource(), new BuildingRestoration());

        Vars.ui.hudGroup.fill(t -> {
            t.name = "auxiliary-functions";
            for (Function function : functions) {
                t.add(function.setTable()).size(50f).tooltip(tt -> {
                    tt.setBackground(Styles.black6);
                    tt.label(() -> function.labelName).pad(2f);
                });
                if (!(Core.app.isAndroid() && Core.settings.getBool("landscape"))) t.row();
            }
            if (Core.app.isAndroid() && Core.settings.getBool("landscape")) {
                t.bottom();
            } else {
                t.right();
            }
        });
        if (Core.app.isAndroid()) {
            Vars.ui.hudGroup.addListener(new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
                    if (Core.settings.getBool("landscape")) {
                        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
                        Vars.ui.hudGroup.fill(t -> {
                            for (Function function : functions) {
                                t.add(function.setTable()).size(50f).tooltip(tt -> {
                                    tt.setBackground(Styles.black6);
                                    tt.label(() -> function.labelName).pad(2f);
                                });
                            }
                            t.bottom();
                        });
                    }
                    return false;
                }
            });
        }
    }
}
