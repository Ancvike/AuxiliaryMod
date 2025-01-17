package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;

public class KeyBind_Keyboard extends Table {
    public static boolean is = false;
    float startX, startY;
    float endX, endY;


    public void init() {

        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyDown(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                Draw.z(Layer.max);
                Lines.stroke(Scl.scl(2f), Color.green);

                endX = World.toTile(Core.input.mouseWorld().x);
                endY = World.toTile(Core.input.mouseWorld().y);
                float width = Math.abs(endX - startX);
                float height = Math.abs(endY - startY);
                Lines.rect(startX, startY, width, height);
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