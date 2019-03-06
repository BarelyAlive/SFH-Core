package mod.sfhcore;

import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry {
	
	public static Block registerBlock(Block block, String modid)
    {
        if (block.getRegistryName() == null)
        	block.setUnlocalizedName(block.getUnlocalizedName());
            block.setRegistryName(block.getUnlocalizedName());
        
        block.registerBlocks();
        block.setRegistryName(block.getUnlocalizedName());
        SFHCoreClientProxy.tryHandleBlockModel(block, block.getUnlocalizedName(), 0, modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
            block.setRegistryName(block.getUnlocalizedName());

            block.registerBlocks();
            itemBlock.registerItems();
            SFHCoreClientProxy.tryHandleBlockModel(block, block.getUnlocalizedName(), no, modid);

        return block;
    }
    
    public static Item registerItem(Item item, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(item.getUnlocalizedName());
            item.setRegistryName(item.getUnlocalizedName());
        item.registerItems();
        SFHCoreClientProxy.tryHandleItemModel(item, item.getUnlocalizedName(), 0, modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, int no, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(item.getUnlocalizedName());
            item.setRegistryName(item.getUnlocalizedName());
            item.registerItems();
        SFHCoreClientProxy.tryHandleItemModel(item, item.getUnlocalizedName(), no, modid);
            
        return item;
    }
}
