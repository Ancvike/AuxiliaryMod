package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Geometry;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.scene.ui.layout.Table;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.core.World;
import mindustry.editor.MapEditor;
import mindustry.game.EventType;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.world.Tile;

import static mindustry.Vars.mobile;
import static mindustry.Vars.world;

public class KeyBind_Keyboard extends Table {
    public static boolean is = false;
    float startX, startY;
    float endX, endY;
    boolean isOpen = false;

    final Vec2[][] brushPolygons = new Vec2[MapEditor.brushSizes.length][0];

    public void init() {

        Events.run(EventType.Trigger.draw, () -> {
            if (Core.input.keyTap(MyKeyBind.RECOVERY_BUDDING.nowKeyCode)) {
                isOpen = !isOpen;
                update();
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

    public void update() {
        if (isOpen) {
            for (int i = 0; i < MapEditor.brushSizes.length; i++) {
                float size = MapEditor.brushSizes[i];
                float mod = size % 1f;
                brushPolygons[i] = Geometry.pixelCircle(size, (index, x, y) -> Mathf.dst(x, y, index - mod, index - mod) <= size - 0.5f);
            }

            Events.run(EventType.Trigger.draw, () -> {
                float scaling = 8;

                Draw.z(Layer.max);

                Tile tile = world.tileWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());
                if (tile == null) return;

                int index = 2;

                Lines.stroke(Scl.scl(2f), Pal.accent);

                if (!mobile) {
                    Lines.poly(brushPolygons[index], tile.x * 8 - 4, tile.y * 8 - 4, scaling);
                }
            });
        }
    }
}