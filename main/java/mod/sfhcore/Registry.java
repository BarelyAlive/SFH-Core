package mod.sfhcore;

import mod.sfhcore.handler.RegisterBlocks;
import mod.sfhcore.handler.RegisterItems;
import mod.sfhcore.handler.RegisterTileEntity;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry {
		
	public static Block registerBlock(Block block)
    {        
        if (block.getRegistryName() != null) {
			RegisterBlocks.blocks.add(block);
		}
        else
        {
        	LogUtil.warn("SFHCore added a block which has a null name!");
        }
		return block;
    }

    public static Block registerBlock(ItemBlock itemBlock)
    {
        Block block = itemBlock.getBlock();

    	
    	if (block.getRegistryName() != null) {
	        RegisterBlocks.blocks.add(block);
    	}
    	else
    	{
    		LogUtil.warn("SFHCore added an itemblock which has a null name!");
    	}
        
        return block;
    }
    
    public static Item registerItem(Item item)
    {
    	if (item.getRegistryName() != null) {
        	RegisterItems.items.add(item);
    	}
    	else
    	{
    		LogUtil.warn("SFHCore added an itemblock which has a null name!");
    	}
            
        return item;
    }
}
