package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.input.KeyCode;
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
import mindustry.game.Gamemode;
import mindustry.gen.*;
import mindustry.graphics.Layer;
import mindustry.ui.Styles;

import static arc.Core.settings;
import static mindustry.Vars.*;

public class HPChange_Mobile_KeyBind extends KeyBind {
    public static Seq<Building> buildings;
    Table changeBuildingsHP = new Table();

    public static boolean shown = false;
    boolean inZoom = false;

    public static boolean isOpen = false;
    float pressedTime = 0f;
    int player_startX, player_endX, player_startY, player_endY;

    public static boolean mobile_deal = false;
    public static boolean isDragged = false;

    Table changeUnitsHP = new Table();
    public static boolean unitsShown = false;

    public HPChange_Mobile_KeyBind() {
        buildBuildingsTable();
        buildUnitsTable();
        Vars.ui.hudGroup.fill(t -> {
            t.add(changeBuildingsHP);
            t.add(changeUnitsHP);
        });

        setupMobileEvents();

        Events.run(EventType.Trigger.draw, () -> {
            if (shown) drawBuilding();
        });
        Events.run(EventType.Trigger.update, () -> {
            if (!Vars.control.input.commandMode) unitsShown = false;
        });
        Events.run(EventType.Trigger.update, () -> {
            if (Vars.control.input.selectedUnits.isEmpty()) unitsShown = false;
        });
    }

    @Override
    void setupMobileEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isOpen) && !isDragged) return;
            player.shooting = false;

            if (Core.input.keyDown(KeyCode.mouseLeft) && isTap) {
                pressedTime += Core.graphics.getDeltaTime();
                if (pressedTime < 0.7f) return;

                player_endX = player.tileX();
                player_endY = player.tileY();
                if (player_startX != player_endX || player_startY != player_endY) return;

                mobile_deal = true;
                handleSelectionDraw(Color.blue, Color.sky);
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(KeyCode.mouseLeft) && isOpen && !isDragged) {
                startSelection();
                player_startX = player.tileX();
                player_startY = player.tileY();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (!shouldHandleInput()) return;
            if (Core.input.keyRelease(KeyCode.mouseLeft) && isOpen && mobile_deal) {
                handleSelectionEnd();
            }
            if (Core.input.keyRelease(KeyCode.mouseLeft) && isOpen) {
                pressedTime = 0f;
                player_startX = 0;
                player_startY = 0;
                player_endX = 0;
                player_endY = 0;
                mobile_deal = false;
                isDragged = false;
            }
        });

        addButton();
    }

    @Override
    void handleSelectionEnd() {
        if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
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
                shown = true;
                changeBuildingsHP.toFront();
                changeBuildingsHP.setLayoutEnabled(true);
            } else {
                buildings = null;
                shown = false;
            }
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
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
                shown = false;
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

        changeBuildingsHP.visible(() -> shown);
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

    private void addButton() {
        Vars.ui.hudGroup.fill(t -> {
            t.name = "mobile-unit";
            t.bottom().left();
            t.button(Icon.android, () -> {
                if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
                    if (!Vars.control.input.selectedUnits.isEmpty()) {
                        changeUnitsHP.parent.setLayoutEnabled(false);
                        unitsShown = true;
                        changeUnitsHP.setLayoutEnabled(true);
                    } else unitsShown = false;
                } else {
                    Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
                }
            }).visible(() -> Vars.control.input.commandMode && !Vars.control.input.selectedUnits.isEmpty()).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "单位修复").pad(2f);
            }).left();
            t.row();
            t.table().size(48f);
        });
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

        changeUnitsHP.visible(() -> unitsShown && Vars.control.input.commandMode && !Vars.control.input.selectedUnits.isEmpty());
    }
}