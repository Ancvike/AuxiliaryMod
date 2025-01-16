package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;

public class KeyBind_Keyboard extends InputHandler {
    public static boolean is = false;
    float startX, startY;
    float endX, endY;

    public void init() {
        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                float cx = Core.camera.position.x, cy = Core.camera.position.y;

                Draw.z(Layer.max);
                Lines.stroke(1f);
                Draw.color(Pal.accent);

                //用自己的方法
                for (int i = (int) (-0.5f * Core.camera.height / 8); i < (int) (0.5f * Core.camera.height / 8); i++) {
                    Lines.line(Mathf.floor((cx - 0.5f * Core.camera.width) / 8) * 8 + 4, Mathf.floor((cy + i * 8) / 8) * 8 + 4, Mathf.floor((cx + 0.5f * Core.camera.width) / 8) * 8 + 4, Mathf.floor((cy + i * 8) / 8) * 8 + 4);
                }
                for (int i = (int) (-0.5f * Core.camera.width / 8); i < (int) (0.5f * Core.camera.width / 8); i++) {
                    Lines.line(Mathf.floor((cx + i * 8) / 8) * 8 + 4, Mathf.floor((cy + 0.5f * Core.camera.height) / 8) * 8 + 4, Mathf.floor((cx + i * 8) / 8) * 8 + 4, Mathf.floor((cy - 0.5f * Core.camera.height) / 8) * 8 + 4);
                }

                Draw.reset();
            }
        });

        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                startX = World.toTile(Core.input.mouseWorld().x);
                startY = World.toTile(Core.input.mouseWorld().y);
            }
        });

        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyRelease(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                //实现方法
                startX = 0;
                startY = 0;
                endX = 0;
                endY = 0;
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
}