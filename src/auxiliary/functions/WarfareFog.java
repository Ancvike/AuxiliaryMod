package auxiliary.functions;

import arc.scene.ui.CheckBox;
import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;

import static mindustry.Vars.state;

public class WarfareFog extends Function {

    public WarfareFog() {
        super(1, new Table(table -> table.add("战争迷雾")));
    }

    @Override
    public Table function() {
        return new Table(t -> {
            CheckBox box = new CheckBox("");
            box.update(() -> box.setChecked(state.rules.fog));
            box.changed(() -> {
                if (Vars.state.rules.sector.isCaptured() || Vars.state.rules.mode() == Gamemode.sandbox)
                    state.rules.fog = !state.rules.fog;
                else Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            });

            t.add(box);
        });
    }
}