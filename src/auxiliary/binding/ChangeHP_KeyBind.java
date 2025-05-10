package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
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
    Seq<Building> buildings = new Seq<>();
    Table changeHP = new Table();
    Table dragTable = new Table();

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
            for (Building building : player.team().data().buildings) {
                if (inZone(building)) {
                    building.health = building.maxHealth;
                }
            }
            Vars.ui.hudfrag.showToast("所选建筑已修复");
        } else {
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
        resetSelection();
    }

    void build() {
        changeHP.setWidth(160f);
        changeHP.setHeight(60f);

        dragTable.table(Tex.buttonEdge1, Table::left).grow();
        changeHP.addListener(new DragListener(changeHP));

        changeHP.add(dragTable).grow();
        changeHP.table(Tex.buttonEdge3, b -> b.button(Icon.cancel, Styles.emptyi, () -> {
        }).grow()).maxWidth(8 * 15f).growY();

        Vars.ui.hudGroup.fill(t -> {
            t.name = "changeHP";
            t.add(changeHP).row();
            t.right();
        });
    }
}