package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.geom.Vec2;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.graphics.Pal;
import mindustry.input.Binding;
import mindustry.input.InputHandler;
import mindustry.input.Placement;
import mindustry.ui.dialogs.BaseDialog;

import static arc.Core.input;
import static auxiliary.binding.MyKeyBind.*;
import static mindustry.Vars.schematics;
import static mindustry.Vars.state;
import static mindustry.input.PlaceMode.breaking;

public class KeyBind_Keyboard extends InputHandler {
    private static boolean isFinished = false;
    public static int schemX = -1;
    public static int schemY = -1;

    public void init() {
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (state.isGame()) {
                if (input.keyDown(RECOVERY_BUDDING.nowKeyCode) && !isFinished) {
                    int rawCursorX = World.toTile(Core.input.mouseWorld().x), rawCursorY = World.toTile(Core.input.mouseWorld().y);
                    if ((Core.input.keyTap(Binding.schematic_select))) {
                        schemX = rawCursorX;
                        schemY = rawCursorY;
                    }
                    if (Core.input.keyDown(Binding.schematic_select) && schemX != -1 && schemY != -1) {
                        int cursorX = tileX(Core.input.mouseX());
                        int cursorY = tileY(Core.input.mouseY());
                        drawSelection(schemX, schemY, cursorX, cursorY, Vars.maxSchematicSize);
                    } else if (Core.input.keyRelease(Binding.schematic_select)) {
                        lastSchematic = schematics.create(schemX, schemY, rawCursorX, rawCursorY);
                        useSchematic(lastSchematic);
                        if (selectPlans.isEmpty()) {
                            lastSchematic = null;
                        }
                        schemX = -1;
                        schemY = -1;
                    }

                    if (input.keyDown(RECOVERY_UNIT.nowKeyCode) && !isFinished) {
                        BaseDialog dialog = new BaseDialog("单位修复");
                        dialog.addCloseButton();
                        dialog.show();
                        isFinished = true;
                    } else if (input.keyRelease(RECOVERY_UNIT.nowKeyCode)) {
                        isFinished = false;
                    }
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

    @Override
    protected void drawSelection(int x1, int y1, int x2, int y2, int maxLength) {
        drawSelection(x1, y1, x2, y2, maxLength, Pal.accentBack, Pal.accent);
    }

    @Override
    protected void drawSelection(int x1, int y1, int x2, int y2, int maxLength, Color col1, Color col2) {
        Placement.NormalizeDrawResult result = Placement.normalizeDrawArea(Blocks.air, x1, y1, x2, y2, false, maxLength, 1f);

        Lines.stroke(2f);

        Draw.color(col1);
        Lines.rect(result.x, result.y - 1, result.x2 - result.x, result.y2 - result.y);
        Draw.color(col2);
        Lines.rect(result.x, result.y, result.x2 - result.x, result.y2 - result.y);
    }
}