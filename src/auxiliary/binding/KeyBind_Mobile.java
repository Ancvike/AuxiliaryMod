package auxiliary.binding;

import arc.Core;
import arc.Events;
import arc.graphics.Color;
import arc.graphics.g2d.Draw;
import arc.graphics.g2d.Lines;
import arc.struct.Seq;
import mindustry.Vars;
import mindustry.content.Blocks;
import mindustry.core.World;
import mindustry.game.EventType;
import mindustry.gen.Icon;
import mindustry.gen.Unit;
import mindustry.input.InputHandler;
import mindustry.input.Placement;
import mindustry.ui.Styles;

import static auxiliary.function.KeyBind_Mobile_Function.isClick;

public class KeyBind_Mobile extends InputHandler{
    boolean isUnitTrue = false;
    int count = 0;

    public void init() {

        Events.run(EventType.Trigger.uiDrawEnd, () -> {
            if (isClick) {
                Placement.NormalizeDrawResult result = Placement.normalizeDrawArea(Blocks.air, World.toTile(Core.input.mouseWorld().x), World.toTile(Core.input.mouseWorld().y), World.toTile(Core.input.mouseWorld().x), World.toTile(Core.input.mouseWorld().y), false, 64, 1f);

                Lines.stroke(2f);

                Draw.color(Color.green);
                Lines.rect(result.x, result.y - 1, result.x2 - result.x, result.y2 - result.y);
                Draw.color(Color.acid);
                Lines.rect(result.x, result.y, result.x2 - result.x, result.y2 - result.y);
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
