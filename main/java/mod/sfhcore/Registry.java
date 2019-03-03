package mod.sfhcore;

import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry {
	
	public static Block registerBlock(Block block, String name, String modid)
    {
        if (block.getRegistryName() == null)
        	block.setUnlocalizedName(name);
            block.setRegistryName(name);
        
        block.registerBlocks();
        block.setRegistryName(name);
        SFHCoreClientProxy.tryHandleBlockModel(block, name, 0, modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, String name, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
        	block.setUnlocalizedName(name);
            block.setRegistryName(name);

            block.registerBlocks();
            itemBlock.registerItems();
            SFHCoreClientProxy.tryHandleBlockModel(block, name, no, modid);

        return block;
    }
    
    public static Item registerItem(Item item, String name, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(name);
            item.setRegistryName(name);
        item.registerItems();
        SFHCoreClientProxy.tryHandleItemModel(item, name, 0, modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, String name, int no, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(name);
            item.setRegistryName(name);
            item.registerItems();
        SFHCoreClientProxy.tryHandleItemModel(item, name, no, modid);
            
        return item;
    }
}
