package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Icon;
import mindustry.gen.Tex;
import mindustry.ui.Styles;

import static mindustry.Vars.mobile;
import static mindustry.Vars.player;

public class ChangeHP_KeyBind extends KeyBind {
    Seq<Building> buildings;

    boolean show = false;
    Table changeHP = new Table();
    Table dragTable = new Table();
    boolean inZoom = false;
    int value = 100;

    public ChangeHP_KeyBind() {
        build();
        if (mobile) {
            setupMobileEvents();
        } else {
            setupDesktopEvents();
        }
    }

    @Override
    void setupMobileEvents() {

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
                show = true;
            } else {
                buildings = null;
                show = false;
            }
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
        resetSelection();
    }

    void build() {
        changeHP.setWidth(400f);
        changeHP.setHeight(200f);

        dragTable.table(Tex.buttonEdge1, t -> {
            t.left();
            t.image().scaling(Scaling.fill).size(20f);
            t.add("改变血量").padLeft(20);
        }).maxHeight(40f).growX();
        dragTable.touchable = Touchable.enabled;
        dragTable.addListener(new DragListener(changeHP));

        changeHP.add(dragTable).grow();
        changeHP.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> {
            show = false;
            inZoom = false;
            buildings = null;
        }).grow()).maxWidth(120f).growY();
        changeHP.row();


        changeHP.table(Styles.black5, t -> {
            Slider slider = new Slider(0f, 10f, 1f, false);
            slider.setValue(10f);
            slider.changed(() -> value = (int) (slider.getValue() * 10));
            slider.moved(v -> {
                for (Building building : buildings) {
                    building.health = building.maxHealth * v * 0.1f;
                }
            });
            t.add(slider);
            t.add(value + "%").grow();
        }).grow();

        changeHP.visible(() -> show);

        Vars.ui.hudGroup.fill(t -> {
            t.name = "changeHP";
            t.add(changeHP).row();
            t.right();
        });
    }
}