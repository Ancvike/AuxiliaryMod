package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.math.Angles;
import arc.math.Mathf;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Time;
import arc.util.Tmp;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.ui.Styles;

import static arc.Core.settings;
import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.player;
import static mindustry.Vars.tilesize;

public class HPChange_KeyBind extends KeyBind {
    public static Seq<Building> buildings;
    Table changeBuildingsHP = new Table();
    public static boolean buildingsShown = false;
    boolean inZoom = false;

    public static boolean isOpen = false;

    public static boolean isDragged = false;

    Table changeUnitsHP = new Table();
    public static boolean unitsShown = false;

    public HPChange_KeyBind() {
        buildBuildingsTable();
        buildUnitsTable();
        Vars.ui.hudGroup.fill(t -> {
            t.add(changeBuildingsHP);
            t.add(changeUnitsHP);
        });

        setupDesktopEvents();

        Events.run(EventType.Trigger.draw, () -> {
            if (buildingsShown) drawBuilding();
        });
        Events.run(EventType.Trigger.update, () -> {
            if (!Vars.control.input.commandMode) unitsShown = false;
        });
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.control.input.selectedUnits.isEmpty()) unitsShown = false;
        });
    }

    @Override
    void setupDesktopEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyDown(MyKeyBind.CHANGE_HP.nowKeyCode) && isTap) {
                handleSelectionDraw(Color.blue, Color.sky);
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.CHANGE_HP.nowKeyCode)) {
                startSelection();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyRelease(MyKeyBind.CHANGE_HP.nowKeyCode)) {
                handleSelectionEnd();
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_UNIT.nowKeyCode)) {
                changeUnitsHP();
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.OPEN_MENU.nowKeyCode)) {
                dialog.show();
            }
        });
    }

    @Override
    void handleSelectionEnd() {
        inZoom = false;
        for (Building building : player.team().data().buildings) {
            if (inZone(building)) {
                inZoom = true;
            }
        }

        if (inZoom) {
            buildings = new Seq<>();
            for (Building building : player.team().data().buildings) {
                if (inZone(building)) {
                    buildings.addAll(building);
                }
            }
            changeBuildingsHP.parent.setLayoutEnabled(false);
            buildingsShown = true;
            changeBuildingsHP.toFront();
            changeBuildingsHP.setLayoutEnabled(true);
        } else {
            buildings = null;
            buildingsShown = false;
        }
        resetSelection();
    }

    public void buildBuildingsTable() {
        changeBuildingsHP.table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("建筑血量修改").padLeft(20);
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> {
                buildingsShown = false;
                inZoom = false;
                buildings = null;
            }).grow()).maxWidth(8 * 15f).growY();

            t.touchable = Touchable.enabled;
            t.addListener(new DragListener(changeBuildingsHP));
        }).height(8 * 6f).growX().prefWidth();

        changeBuildingsHP.row();
        changeBuildingsHP.table(Styles.black5, table -> {
            table.top().left();

            table.table(rules -> {
                rules.top().left();

                Label label = rules.add("100%").get();
                Slider slider = new Slider(0, 10, 1, false);
                slider.setValue(10f);
                slider.changed(() -> {
                    isDragged = true;
                    label.setText((int) (slider.getValue() * 10) + "%");
                });
                slider.change();
                slider.moved(hp -> {
                    isDragged = true;
                    for (Building building : buildings) {
                        building.health = building.maxHealth * (int) hp * 0.1f;
                    }
                });
                rules.add(slider);
            }).grow();
        }).grow();

        changeBuildingsHP.visible(() -> buildingsShown);
    }

    public void drawBuilding() {
        for (Building building : buildings) {
            Draw.z(Layer.max);
            Draw.color(Tmp.c1.set(Color.blue).lerp(Color.sky, Mathf.absin(Time.time, 3f, 1f)).a(settings.getInt("selectopacity") / 100f));

            float length = building.block.size * tilesize;

            for (int i = 0; i < 4; i++) {
                float rot = i * 90f + 45f + (-Time.time) % 360f;
                Draw.rect("select-arrow", building.x() + Angles.trnsx(rot, length), building.y() + Angles.trnsy(rot, length), length / 1.9f, length / 1.9f, rot - 135f);
            }
        }
        Draw.color();
    }

    public void buildUnitsTable() {
        changeUnitsHP.table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("单位血量修改").padLeft(20);
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> unitsShown = false).grow()).maxWidth(8 * 15f).growY();

            t.touchable = Touchable.enabled;
            t.addListener(new DragListener(changeUnitsHP));
        }).height(8 * 6f).growX().prefWidth();

        changeUnitsHP.row();
        changeUnitsHP.table(Styles.black5, table -> {
            table.top().left();

            table.table(rules -> {
                rules.top().left();

                Label label = rules.add("100%").get();
                Slider slider = new Slider(0, 10, 1, false);
                slider.setValue(10f);
                slider.changed(() -> {
                    isDragged = true;
                    label.setText((int) (slider.getValue() * 10) + "%");
                });
                slider.change();
                slider.moved(hp -> {
                    isDragged = true;
                    for (Unit unit : Vars.control.input.selectedUnits) {
                        unit.health = unit.maxHealth * (int) hp * 0.1f;
                    }
                });
                rules.add(slider);
            }).grow();
        }).grow();

        changeUnitsHP.visible(() -> unitsShown);
    }

    private void changeUnitsHP() {
        if (!Vars.control.input.selectedUnits.isEmpty()) {
            changeUnitsHP.parent.setLayoutEnabled(false);
            unitsShown = true;
            changeUnitsHP.setLayoutEnabled(true);
        } else unitsShown = false;
    }
}