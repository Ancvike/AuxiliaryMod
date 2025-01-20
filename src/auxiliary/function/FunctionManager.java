package auxiliary.function;

import arc.Core;
import arc.Events;
import arc.input.KeyCode;
import arc.math.geom.Vec2;
import arc.scene.event.InputEvent;
import arc.scene.event.InputListener;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Tmp;
import auxiliary.binding.KeyBind_Mobile;
import mindustry.Vars;

import mindustry.game.EventType;
import mindustry.ui.Styles;

import static mindustry.Vars.mobile;
import static auxiliary.function.UIMovement.isDragged;

public class FunctionManager {
    public static final Seq<Function> functions = new Seq<>();
    public static Table table;

    public static void init() {
//        if (Vars.mobile) {
            functions.addAll(new UIMovement(), new FullResource(), new Restoration(), new KeyBind_Mobile());
//        } else {
//            functions.addAll(new UIMovement(), new FullResource(), new Restoration());
//        }

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
                    table.find("ui-move").addListener(new DragHandleListener(table));
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
                    table.find("ui-move").addListener(new DragHandleListener(table));
                }
            });
        }

        table.find("ui-move").addListener(new DragHandleListener(table));
    }
}

class DragHandleListener extends InputListener {
    protected float lastX, lastY;
    final Table table;

    public DragHandleListener(Table table) {
        this.table = table;
    }

    @Override
    public boolean touchDown(InputEvent event, float x, float y, int pointer, KeyCode button) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(x, y));
        lastX = v.x;
        lastY = v.y;
        table.toFront();
        isDragged = false;
        return true;
    }

    @Override
    public void touchDragged(InputEvent event, float dx, float dy, int pointer) {
        Vec2 v = event.listenerActor.localToStageCoordinates(Tmp.v1.set(dx, dy));
        table.setPosition(table.x + (v.x - lastX), table.y + (v.y - lastY));
        lastX = v.x;
        lastY = v.y;
        isDragged = true;
    }
}