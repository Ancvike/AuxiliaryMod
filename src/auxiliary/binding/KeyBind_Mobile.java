package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.math.Mathf;
import arc.math.geom.Vec2;
import arc.scene.ui.layout.Scl;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.editor.MapEditor;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.graphics.Layer;
import mindustry.graphics.Pal;
import mindustry.input.InputHandler;
import mindustry.ui.Styles;
import mindustry.world.Block;
import mindustry.world.Tile;

import static mindustry.Vars.world;

public class KeyBind_Mobile extends InputHandler {
    boolean isUnitTrue = false;
    int count = 0;

    public static Block drawBlock = Blocks.router;
    EditorTool tool;
    final Vec2[][] brushPolygons = new Vec2[MapEditor.brushSizes.length][0];

    public void init() {

        Events.run(EventType.Trigger.draw, () -> {
            float cx = Core.camera.position.x, cy = Core.camera.position.y;
            float scaling = 8;

            Draw.z(Layer.max);

            Lines.stroke(1f);
            Draw.color(Pal.accent);
            for (int i = (int) (-0.5f * Core.camera.height / 8); i < (int) (0.5f * Core.camera.height / 8); i++) {
                Lines.line(Mathf.floor((cx - 0.5f * Core.camera.width) / 8) * 8 + 4, Mathf.floor((cy + i * 8) / 8) * 8 + 4, Mathf.floor((cx + 0.5f * Core.camera.width) / 8) * 8 + 4, Mathf.floor((cy + i * 8) / 8) * 8 + 4);
            }
            for (int i = (int) (-0.5f * Core.camera.width / 8); i < (int) (0.5f * Core.camera.width / 8); i++) {
                Lines.line(Mathf.floor((cx + i * 8) / 8) * 8 + 4, Mathf.floor((cy + 0.5f * Core.camera.height) / 8) * 8 + 4, Mathf.floor((cx + i * 8) / 8) * 8 + 4, Mathf.floor((cy - 0.5f * Core.camera.height) / 8) * 8 + 4);
            }
            Draw.reset();

            Tile tile = world.tileWorld(Core.input.mouseWorldX(), Core.input.mouseWorldY());

            Lines.stroke(Scl.scl(2f), Pal.accent);

            if (!drawBlock.isMultiblock() || tool == EditorTool.eraser) {
                if (tool.edit) {
                    Lines.poly(brushPolygons[2], tile.x * 8 - 4, tile.y * 8 - 4, scaling);
                }
            }
        });

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            isUnitTrue = Vars.control.input.commandMode;
            if (isUnitTrue && count == 0) {
                Vars.ui.hudGroup.fill(t -> {
                    t.name = "mobile-unit";
                    t.bottom();
                    t.left();
                    t.button(Icon.android, this::unitClick).size(50f).tooltip(tt -> {
                        tt.setBackground(Styles.black6);
                        tt.label(() -> "单位修复").pad(2f);
                    }).left();
                    t.row();
                    t.table().size(48f);
                });
                count++;
            }
        });
        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            isUnitTrue = Vars.control.input.commandMode;
            if (!isUnitTrue && count != 0) {
                count = 0;
                Vars.ui.hudGroup.removeChild(Vars.ui.hudGroup.find("mobile-unit"));
            }
        });
    }


    public void unitClick() {
        Seq<Unit> selectedUnits = Vars.control.input.selectedUnits;
        for (Unit unit : selectedUnits) {
            unit.health = unit.maxHealth;
        }
        Vars.ui.hudfrag.showToast("所选单位已修复");
    }
}

enum EditorTool {
    eraser("eraseores") {
        {
            edit = true;
        }
    };

    /**
     * All the internal alternate placement modes of this tool.
     */
    public final String[] altModes;
    /**
     * Whether this tool causes canvas changes when touched.
     */
    public boolean edit;

    EditorTool(String... altModes) {
        this.altModes = altModes;
    }
}