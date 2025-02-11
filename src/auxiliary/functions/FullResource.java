package auxiliary.functions;

import mindustry.Vars;
import mindustry.gen.Icon;
import mindustry.type.Item;
import mindustry.world.blocks.storage.CoreBlock;

import static auxiliary.functions.Menu.dialog;
import static mindustry.Vars.state;

public class FullResource extends Function {

    public FullResource() {
        super(0, "核心资源最大化");
    }

    @Override
    public void onClick() {
        if (!state.rules.waves && state.isCampaign()) {
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
            Vars.ui.hudfrag.showToast(Icon.cancel, "区块未占领,无法使用该功能");
        }
    }
}