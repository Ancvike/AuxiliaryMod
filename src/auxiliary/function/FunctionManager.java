package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;

import mindustry.game.EventType;
import mindustry.ui.Styles;

import static mindustry.Vars.mobile;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();
    public static Table table;

    public static void init() {
        functions.addAll(new UIMovement(), new FullResource(), new Restoration());

        if (mobile && Core.settings.getBool("landscape")) {
            table = new Table(t -> {
                for (Function function : functions) {
                    t.add(function.setTable()).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> function.labelName).pad(2f);
                    });
                }
                t.setSize(functions.size * 50f, 50f);
            });
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions-mobile-landscape";
                t.add(table);
                t.bottom();
            });
        } else {
            table = new Table(t -> {
                for (Function function : functions) {
                    t.add(function.setTable()).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> function.labelName).pad(2f);
                    });
                    t.row();
                }
                t.setSize(50f, functions.size * 50f);
            });
            Vars.ui.hudGroup.fill(t -> {
                t.name = "auxiliary-functions";
                t.add(table);
                t.right();
            });
        }

        if (mobile) {
            Events.run(EventType.Trigger.uiDrawEnd, () -> {
                if (Core.settings.getBool("landscape") && Vars.ui.hudGroup.find("auxiliary-functions") != null) {
                    Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions"));
                    table = new Table(t -> {
                        for (Function function : functions) {
                            t.add(function.setTable()).size(50f).tooltip(tt -> {
                                tt.setBackground(Styles.black6);
                                tt.label(() -> function.labelName).pad(2f);
                            });
                        }
                        t.setSize(functions.size * 50f, 50f);
                    });
                    Vars.ui.hudGroup.fill(t -> {
                        t.name = "auxiliary-functions-mobile-landscape";
                        t.add(table);
                        t.bottom();
                    });
                } else if (!Core.settings.getBool("landscape") && Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape") != null) {
                    Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("auxiliary-functions-mobile-landscape"));
                    table = new Table(t -> {
                        for (Function function : functions) {
                            t.add(function.setTable()).size(50f).tooltip(tt -> {
                                tt.setBackground(Styles.black6);
                                tt.label(() -> function.labelName).pad(2f);
                            });
                            t.row();
                        }
                        t.setSize(50f, functions.size * 50f);
                    });
                    Vars.ui.hudGroup.fill(t -> {
                        t.name = "auxiliary-functions";
                        t.add(table);
                        t.right();
                    });
                }
            });
        }
    }
}