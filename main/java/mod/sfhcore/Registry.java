package mod.sfhcore;

import mod.sfhcore.handler.RegisterBlocks;
import mod.sfhcore.handler.RegisterItems;
import mod.sfhcore.handler.RegisterTileEntity;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry {
		
	public static Block registerBlock(Block block)
    {        
        RegisterBlocks.blocks.add(block);
                
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock)
    {
        Block block = itemBlock.getBlock();
        RegisterBlocks.blocks.add(block);
        
        return block;
    }
    
    public static Item registerItem(Item item)
    {
        RegisterItems.items.add(item);
            
        return item;
    }
}
