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
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.gen.Unit;
import mindustry.graphics.Drawf;
import mindustry.graphics.Layer;
import mindustry.input.InputHandler;
import mindustry.ui.Styles;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.settings;
import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.*;

public class HPChange_KeyBind extends InputHandler {
    int startX, endX, startY, endY;
    boolean isTap = false;
    boolean inZoom = false;

    public static Seq<Building> buildings;
    Table changeBuildingsHP = new Table();
    public static boolean buildingsShown = false;

    public static boolean isDragged = false;

    Table changeUnitsHP = new Table();
    public static boolean unitsShown = false;

    //-- mobile--
    public static boolean isOpen = false;
    float pressedTime = 0f;
    int player_startX, player_endX, player_startY, player_endY;
    public static boolean mobile_deal = false;
    BaseDialog baseDialog = new BaseDialog("");
    public HPChange_KeyBind() {
        Vars.ui.hudGroup.fill(t -> {

            baseDialog.addCloseButton();
            t.button("00", () -> {
                baseDialog.cont.add();
                baseDialog.show();
            });
        });
        //调试代码

        buildBuildingsTable();
        buildUnitsTable();
        Vars.ui.hudGroup.fill(t -> {
            t.add(changeBuildingsHP);
            t.add(changeUnitsHP);
        });

        if (mobile) setupMobileEvents();
        else setupDesktopEvents();

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

    void setupMobileEvents() {
        Events.run(EventType.Trigger.draw, () -> {
            if (!(shouldHandleInput() && isOpen) && isDragged) return;
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
                player.shooting = false;

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
                player.shooting = true;
            }
        });

        addButton();
    }

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
            changeBuildingsHP.setLayoutEnabled(true);
        } else {
            buildings = null;
            buildingsShown = false;
        }
        resetSelection();
    }

    boolean shouldHandleInput() {
        return !Vars.ui.chatfrag.shown() && // 未打开聊天框
                Core.scene.getKeyboardFocus() == null && // 无文本输入焦点
                Vars.ui.hudfrag.shown && // HUD正常显示
                Vars.state.isPlaying() && // 游戏进行中
                Vars.player != null; // 玩家实体存在
    }

    boolean inZone(Building building) {
        int x = World.toTile(building.x);
        int y = World.toTile(building.y);
        return ((x >= startX && x <= endX) || (x <= startX && x >= endX)) && ((y >= startY && y <= endY) || (y <= startY && y >= endY));
    }

    void resetSelection() {
        startX = startY = endX = endY = 0;
        isTap = false;
    }

    void startSelection() {
        player.shooting = false;
        startX = World.toTile(Core.input.mouseWorld().x);
        startY = World.toTile(Core.input.mouseWorld().y);
        isTap = true;
    }

    void handleSelectionDraw(Color color1, Color color2) {
        player.shooting = false;
        endX = World.toTile(Core.input.mouseWorld().x);
        endY = World.toTile(Core.input.mouseWorld().y);
        drawSelection(startX, startY, endX, endY, 1000, color1, color2);

        for (Building building : player.team().data().buildings) {
            if (inZone(building)) {
                Drawf.selected(building, color2);
            }
        }
    }

    public void buildBuildingsTable() {
        changeBuildingsHP.table(t -> {
            t.table(Tex.buttonEdge1, b -> {
                b.left();
                b.add("建筑血量修改");
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
                    baseDialog.cont.add("isDragged = true (change)");
                    baseDialog.cont.row();
                    isDragged = true;
                    label.setText((int) (slider.getValue() * 10) + "%");
                });
                slider.moved(hp -> {
                    baseDialog.cont.add("isDragged = true (moved)");
                    baseDialog.cont.row();
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
                b.add("单位血量修改");
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
                slider.changed(() -> label.setText((int) (slider.getValue() * 10) + "%"));
                slider.moved(hp -> {
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

    private void addButton() {
        Vars.ui.hudGroup.fill(t -> {
            t.name = "mobile-unit";
            t.bottom().left();
            t.button(Icon.android, () -> {
                if (!Vars.control.input.selectedUnits.isEmpty()) {
                    changeUnitsHP.parent.setLayoutEnabled(false);
                    unitsShown = true;
                    changeUnitsHP.setLayoutEnabled(true);
                } else unitsShown = false;
            }).visible(() -> Vars.control.input.commandMode && !Vars.control.input.selectedUnits.isEmpty()).size(50f).tooltip(tt -> {
                tt.setBackground(Styles.black6);
                tt.label(() -> "单位修复").pad(2f);
            }).left();
            t.row();
            t.table().size(48f);
        });
    }
}