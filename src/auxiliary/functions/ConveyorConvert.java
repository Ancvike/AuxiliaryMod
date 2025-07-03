package auxiliary.functions;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.ui.dialogs.BaseDialog;

public class ConveyorConvert extends Function {
    BaseDialog dialog = new BaseDialog("传送带替换");

    public ConveyorConvert() {
        super(1, new Table(table -> table.add("传送带替换")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox || Vars.state.rules.mode() == Gamemode.editor) {
                buildDialog();
//                dialog.show();
            } else {
                dialog.hide();
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }

    private void buildDialog() {
        dialog.cont.clear();
    }
}
