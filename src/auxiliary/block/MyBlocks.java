package auxiliary.block;

import arc.Core;
import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.storage.Unloader;

import static mindustry.type.ItemStack.with;

public class MyBlocks {

    public Block omnipotent_unloader;

    public void load() {
        omnipotent_unloader = new Unloader("114514") {{
            requirements(Category.distribution, with(Items.graphite, 20, Items.silicon, 20, Items.tungsten, 10));
            alwaysUnlocked = true;
            size = 1;
        }};
    }
}
