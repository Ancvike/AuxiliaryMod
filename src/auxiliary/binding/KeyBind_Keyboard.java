package auxiliary.binding;

import arc.Core;
import arc.Events;
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

    public void init() {
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (Vars.state.isGame()) {
                int selectX = -1;
                int selectY = -1;
                if (Core.input.keyDown(RECOVERY_BUDDING.nowKeyCode)) {
                    BaseDialog dialog = new BaseDialog("选择区域");
                    dialog.addCloseButton();
                    dialog.show();
//                    selectX = tileX(Core.input.mouseX());
//                    selectY = tileY(Core.input.mouseY());
//                    int schemX = World.toTile(Core.input.mouseWorld().x);
//                    int schemY = World.toTile(Core.input.mouseWorld().y);
                } else if (Core.input.keyRelease(RECOVERY_BUDDING.nowKeyCode)) {
                    int cursorX = tileX(Core.input.mouseX());
                    int cursorY = tileY(Core.input.mouseY());
                    removeSelection(selectX, selectY, cursorX, cursorY, !Core.input.keyDown(Binding.schematic_select) ? 100 : Vars.maxSchematicSize);
                    if (lastSchematic != null) {
                        useSchematic(lastSchematic);
                        lastSchematic = null;
                    }
                    selectX = -1;
                    selectY = -1;
                }

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