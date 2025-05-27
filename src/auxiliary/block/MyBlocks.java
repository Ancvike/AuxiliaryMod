package auxiliary.block;

import mindustry.type.Category;
import mindustry.world.Block;
import mindustry.world.blocks.storage.Unloader;

public class MyBlocks {

    public Block omnipotent_unloader = new Unloader("omnipotent_unloader");

    public void load() {
        omnipotent_unloader.size = 2;
        omnipotent_unloader.health = 100;
        omnipotent_unloader.category = Category.effect;
        omnipotent_unloader.load();
    }
}
