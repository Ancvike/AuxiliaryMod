package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.event.Touchable;
import arc.scene.ui.Label;
import arc.scene.ui.Slider;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import arc.util.Scaling;
import auxiliary.functions.dragFunction.DragListener;
import mindustry.Vars;
import mindustry.game.EventType;
import mindustry.game.Gamemode;
import mindustry.gen.Building;
import mindustry.gen.Groups;
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
        changeHP.setWidth(160f);
        changeHP.setHeight(60f);

        dragTable.table(Tex.buttonEdge1, b -> {
            b.left();
            b.image().scaling(Scaling.fill).size(20f);
            b.add(Core.bundle.get("adadad")).padLeft(20);
        }).grow();
        dragTable.touchable = Touchable.enabled;
        dragTable.addListener(new DragListener(changeHP));
        changeHP.add(dragTable);

        changeHP.table(Tex.buttonEdge3, t -> t.button(Icon.cancel, Styles.emptyi, () -> show = false).grow()).maxWidth(8 * 15f).growY();

        changeHP.row();

        changeHP.table(Styles.black5, pt -> pt.pane(Styles.noBarPane, new Table(t -> {
            t.top().left();

            t.table(rules -> {
                rules.top().left();

                Label label = rules.add("Block Health: ").get();
                Slider slider = new Slider(0, 100, 1, false);
                slider.changed(() -> label.setText("Block Health: " + (int) slider.getValue() + "%"));
                slider.change();
                slider.moved(hp -> Groups.build.each(b -> b.health(b.block.health * hp / 100)));
                rules.add(slider);
            }).grow();
        })).grow()).grow();
    }
}