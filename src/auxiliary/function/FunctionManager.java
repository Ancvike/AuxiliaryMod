package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import arc.util.Time;
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
            while (true) {
                Events.on(EventType.WorldLoadEvent.class, e -> {
                    time += Time.delta;
                    if (time >= 1f) {
                        time = 0;
                        BaseDialog dialog = new BaseDialog("");
                        dialog.addCloseButton();
                        dialog.show();
//                    if (Core.settings.getBool("landscape")) {
//                        Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
//                        Vars.ui.hudGroup.fill(t -> {
//                            t.name = "auxiliary-functions";
//                            for (Function function : functions) {
//                                t.add(function.setTable()).size(50f).tooltip(tt -> {
//                                    tt.setBackground(Styles.black6);
//                                    tt.label(() -> function.labelName).pad(2f);
//                                });
//                                if (!(mobile && Core.settings.getBool("landscape"))) t.row();
//                            }
//                            if (mobile && Core.settings.getBool("landscape")) {
//                                t.bottom();
//                            } else {
//                                t.right();
//                            }
//                        });
//                    }
                    }
                });
            }
        }
    }
}

