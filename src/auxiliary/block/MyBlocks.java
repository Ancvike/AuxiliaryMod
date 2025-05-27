package auxiliary.block;

import mindustry.content.Items;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.meta.BlockGroup;

import static mindustry.type.ItemStack.with;

public class MyBlocks {

    public Block omnipotent_unloader = new Unloader("omnipotent_unloader") {{
        requirements(Category.effect, with(Items.graphite, 20, Items.silicon, 20, Items.tungsten, 10));
        speed = 60f / 11f;
        group = BlockGroup.transportation;
        category = Category.effect;
    }};
}
