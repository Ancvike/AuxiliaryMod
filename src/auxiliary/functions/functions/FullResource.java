package auxiliary.functions.functions;

import arc.scene.ui.layout.Table;
import auxiliary.functions.Function;
import mindustry.Vars;
import mindustry.type.Item;
import mindustry.world.blocks.storage.CoreBlock;

import static auxiliary.functions.Menu.dialog;

public class FullResource extends Function {

    public FullResource() {
        super(0, new Table(table -> table.add("核心内资源最大化")));
    }

    @Override
    public Table function() {
        return new Table(t -> t.button("使用", () -> {
            CoreBlock.CoreBuild core = Vars.player.team().core();
            if (core == null || core.items == null) {
                return;
            }
            for (Item item : Vars.content.items()) {
                if (!Vars.player.team().items().has(item)) continue;
                core.items.set(item, core.storageCapacity);
            }
            Vars.ui.hudfrag.showToast("核心资源已到达最大值");
            dialog.hide();
        }).width(200f));
    }
}