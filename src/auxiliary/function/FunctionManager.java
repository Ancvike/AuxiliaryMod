package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.scene.event.EventListener;
import arc.scene.event.SceneEvent;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.game.EventType;
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
        Events.on(EventType.WorldLoadEndEvent.class, e -> {
            if (Core.app.isAndroid()) {
                Vars.ui.settings.addListener(new EventListener() {
                    @Override
                    public boolean handle(SceneEvent sceneEvent) {
                        if (Core.app.isAndroid() && Core.settings.getBool("landscape")) {
                            Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
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
                        }
                        return false;
                    }
                });
            }
        });
    }
}
