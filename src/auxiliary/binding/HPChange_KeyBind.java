package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.input.KeyCode;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.ui.Styles;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.*;

public class HPChange_KeyBind extends KeyBind {
    public static Seq<Building> buildings;
    Table changeHP = new Table();
    boolean shown = false;
    boolean inZoom = false;

    public static boolean isOpen = false;

    private float pressTime = 0f;
    private int player_startX, player_startY;
    private int player_endX, player_endY;
    public static boolean isMoved = false;

    public HPChange_KeyBind() {
        buildTable();
        Vars.ui.hudGroup.fill(t -> t.add(changeHP).right());
        if (mobile) {
            setupMobileEvents();
        } else {
            setupDesktopEvents();
        }
    }

    @Override
    void setupMobileEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isTap && isOpen)) return;

            if (Core.input.keyDown(KeyCode.mouseLeft)) pressTime += Core.graphics.getDeltaTime();

            if (pressTime < 0.7f) return;

            player_endX = player.tileX();
            player_endY = player.tileY();

            if (pressTime >= 0.7f && player_endX != player_startX && player_endY != player_startY) {
                isMoved = true;
            }

            if (Core.input.keyDown(KeyCode.mouseLeft) && !isMoved) {
                handleSelectionDraw(Color.blue, Color.sky);
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (shouldHandleInput() && Core.input.keyTap(KeyCode.mouseLeft) && isOpen) {
                player_startX = player.tileX();
                player_startY = player.tileY();
                startSelection();
            }
        });
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isOpen)) return;

            if (pressTime < 0.7f) return;

            if (Core.input.keyRelease(KeyCode.mouseLeft) && !isMoved) {
                handleSelectionEnd();
            }

            if (Core.input.keyRelease(KeyCode.mouseLeft)) {
                player_startX = 0;
                player_startY = 0;
                pressTime = 0f;
                isMoved = false;
            }
        });

        addUnitHealButton();
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
            if (shouldHandleInput() && Core.input.keyTap(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode) {
                healSelectedUnits();
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
                changeHP.parent.setLayoutEnabled(false);
                shown = true;
                changeHP.toFront();
                changeHP.setLayoutEnabled(true);
            } else {
                buildings = null;
            }
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
        resetSelection();
    }

    public void buildTable() {
        changeHP.table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("血量修改").padLeft(20);
            }).grow();

            t.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> {
                shown = false;
                inZoom = false;
                buildings = null;
            }).grow()).maxWidth(8 * 15f).growY();

            t.touchable = Touchable.enabled;
            t.addListener(new DragListener(changeHP));
        }).height(8 * 6f).growX().prefWidth();

        changeHP.row();
        changeHP.table(Styles.black5, table -> {
            table.top().left();

            table.table(rules -> {
                rules.top().left();

                Label label = rules.add("100%").get();
                Slider slider = new Slider(0, 10, 1, false);
                slider.setValue(10f);
                slider.changed(() -> {
                    isMoved = false;
                    label.setText((int) (slider.getValue() * 10) + "%");
                });
                slider.change();
                slider.moved(hp -> {
                    isMoved = false;
                    for (Building building : buildings) {
                        building.health = building.maxHealth * (int) hp * 0.1f;
                    }
                });
                rules.add(slider);
            }).grow();
        }).grow();

        changeHP.visible(() -> shown);
    }

    private void addUnitHealButton() {
        Vars.ui.hudGroup.fill(t -> {
            t.name = "mobile-unit";
            t.bottom().left();
            t.button(Icon.android, () -> {
                if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
                    Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                    for (Unit unit : selectedUnits) {
                        unit.health = unit.maxHealth;
                    }
                    Vars.ui.hudfrag.showToast("所选单位已修复");
                } else {
                    Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
                }
            }).visible(() -> Vars.control.input.commandMode).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "单位修复").pad(2f);
            }).left();
            t.row();
            t.table().size(48f);
        });
    }

    private void healSelectedUnits() {
        if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
            Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
            for (Unit unit : selectedUnits) {
                unit.health = unit.maxHealth;
            }
            Vars.ui.hudfrag.showToast("所选单位已修复");
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
    }
}