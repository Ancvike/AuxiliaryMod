package auxiliary.block;

import mindustry.content.Items;
import mindustry.content.TechTree;
import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.storage.Unloader;
import mindustry.world.meta.BlockGroup;

import static mindustry.content.Planets.erekir;
import static mindustry.type.ItemStack.with;

public class MyBlocks {

    Block omnipotent_unloader = new Unloader("unloader") {{
        requirements(Category.effect, with(Items.graphite, 20, Items.silicon, 20, Items.tungsten, 10));
        speed = 60f / 11f;
        group = BlockGroup.transportation;
    }};

    public void load() {
        erekir.techTree.children.add(new TechTree.TechNode(null, omnipotent_unloader, null));
    }
}
