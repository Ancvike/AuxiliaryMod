package auxiliary.functions;

import arc.scene.ui.layout.Table;
import mindustry.Vars;
import mindustry.game.Gamemode;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.world.blocks.storage.CoreBlock;

import static auxiliary.functions.Menu.dialog;

public class FullResource extends Function {

    public FullResource() {
        super(1, new Table(table -> table.add("核心内资源最大化")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            if ((Vars.state.rules.sector != null && Vars.state.rules.sector.isCaptured()) || Vars.state.rules.mode() == Gamemode.sandbox) {
                CoreBlock.CoreBuild core = Vars.player.team().core();
                if (core == null || core.items == null) {
                    return;
                }
                for (int i = 0; i < Vars.content.items().size; i++) {
                    Item item = Vars.content.item(i);
                    if (!Vars.player.team().items().has(item)) continue;
                    core.items.set(item, core.storageCapacity);
                }
                Vars.ui.hudfrag.showToast("核心资源已到达最大值");
                dialog.hide();
            } else {
                dialog.hide();
                Vars.ui.hudfrag.showToast(Icon.cancel, "[scarlet]区块未占领,无法使用该功能");
            }
        }).width(200f));
    }
}