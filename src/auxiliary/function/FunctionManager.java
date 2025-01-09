package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.struct.Seq;
import mindustry.Vars;

import mindustry.game.EventType;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static mindustry.Vars.mobile;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();

    public static void init() {
        functions.addAll(new FullResource(), new BuildingRestoration());

        if (mobile && Core.settings.getBool("landscape")) {
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions-mobile-landscape";
                for (Function function : functions) {
                    t.add(function.setTable()).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> function.labelName).pad(2f);
                    });
                }
                t.bottom();
                t.right();
            });
        } else {
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions";
                for (Function function : functions) {
                    t.add(function.setTable()).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> function.labelName).pad(2f);
                    });
                    t.row();
                }
                t.right();
            });
        }
        if (mobile) {
            Events.run(EventType.Trigger.uiDrawEnd, () -> {
                if (Core.settings.getBool("landscape") && Vars.ui.hudGroup.find("auxiliary-functions") != null) {
                    Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
                    Vars.ui.hudGroup.fill(t -> {
                        t.name = "auxiliary-functions-mobile-landscape";
                        for (Function function : functions) {
                            t.add(function.setTable()).size(50f).tooltip(tt -> {
                                tt.setBackground(Styles.black6);
                                tt.label(() -> function.labelName).pad(2f);
                            });
                        }
                        t.bottom();
                        t.right();
                    });
                    BaseDialog dialog = new BaseDialog("s->h");
                    dialog.addCloseButton();
                    dialog.show();
                } else if (!Core.settings.getBool("landscape") && Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape") != null) {
                    Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape"));
//                    Vars.ui.hudGroup.fill(t -> {
//                        t.name = "auxiliary-functions";
//                        for (Function function : functions) {
//                            t.add(function.setTable()).size(50f).tooltip(tt -> {
//                                tt.setBackground(Styles.black6);
//                                tt.label(() -> function.labelName).pad(2f);
//                            });
//                            t.row();
//                        }
//                        t.right();
//                    });
                    BaseDialog dialog = new BaseDialog("h->s");
                    dialog.addCloseButton();
                    dialog.show();
                }
            });
        }
    }
}