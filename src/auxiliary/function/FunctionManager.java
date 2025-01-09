package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;

import mindustry.game.EventType;
import mindustry.ui.Styles;

import static mindustry.Vars.mobile;

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
                if (!(mobile && Core.settings.getBool("landscape"))) t.row();
            }
            if (mobile && Core.settings.getBool("landscape")) {
                t.bottom();
            } else {
                t.right();
            }
        });

//        if (mobile && Core.settings.getBool("landscape")) {
            Events.run(EventType.Trigger.uiDrawEnd, () -> {
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
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
            });
//        }
    }
}

class MyEventType {
}