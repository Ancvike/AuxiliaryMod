package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.game.EventType;
import mindustry.gen.Building;

import static mindustry.Vars.mobile;

public class ChangeHP_KeyBind extends KeyBind {
    Seq<Building> buildings = new Seq<>();
    Table changeHP = new Table();

    public ChangeHP_KeyBind() {
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
    }
}
