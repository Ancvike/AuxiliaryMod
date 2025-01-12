package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.input.Binding;
import mindustry.input.InputHandler;
import mindustry.ui.dialogs.BaseDialog;

import static auxiliary.binding.MyKeyBind.CONVEYOR_CHANGE;
import static auxiliary.binding.MyKeyBind.RECOVERY_BUDDING;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;
    int schemX = -1, schemY = -1;
    static final BaseDialog dialog = new BaseDialog("");

    public void init() {
        dialog.addCloseButton();
        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(CONVEYOR_CHANGE.nowKeyCode) && !is) {
                dialog.show();
                is = true;
            } else if (Core.input.keyRelease(CONVEYOR_CHANGE.nowKeyCode)) {
                is = false;
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            Lines.stroke(1f);
            int cursorX = tileX(Core.input.mouseX());
            int cursorY = tileY(Core.input.mouseY());

            if (!Core.scene.hasKeyboard()) {
                if (Core.input.keyDown(Binding.rebuild_select)) {
                    drawRebuildSelection(schemX, schemY, cursorX, cursorY);
                }
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (!Core.scene.hasKeyboard() && schemX != -1 && schemY != -1) {
                if (Core.input.keyRelease(RECOVERY_BUDDING.nowKeyCode)) {
                    rebuildArea(schemX, schemY, World.toTile(Core.input.mouseWorld().x), World.toTile(Core.input.mouseWorld().y));
                    schemX = -1;
                    schemY = -1;
                    Vars.ui.hudfrag.showToast("所选建筑已修复");
                    dialog.cont.add("KeyRelease触发");
                }
            }

            if (Core.input.keyTap(RECOVERY_BUDDING.nowKeyCode) && !Core.scene.hasKeyboard()) {
                schemX = World.toTile(Core.input.mouseWorld().x);
                schemY = World.toTile(Core.input.mouseWorld().y);
                dialog.cont.add("KeyTap触发");
            }
        });

        Events.run(EventType.Trigger.update, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_UNIT.nowKeyCode) && Vars.control.input.commandMode && !is) {
                Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
                for (Unit unit : selectedUnits) {
                    unit.health = unit.maxHealth;
                }
                Vars.ui.hudfrag.showToast("所选单位已修复");
                is = true;
            } else if (Core.input.keyRelease(MyKeyBind.RECOVERY_UNIT.nowKeyCode)) {
                is = false;
            }
        });
    }

    int tileX(float cursorX) {
        Vec2 vec = Core.input.mouseWorld(cursorX, 0);
        if (selectedBlock()) {
            vec.sub(block.offset, block.offset);
        }
        return World.toTile(vec.x);
    }

    int tileY(float cursorY) {
        Vec2 vec = Core.input.mouseWorld(0, cursorY);
        if (selectedBlock()) {
            vec.sub(block.offset, block.offset);
        }
        return World.toTile(vec.y);
    }
}