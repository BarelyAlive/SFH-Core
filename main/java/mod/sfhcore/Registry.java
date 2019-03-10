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
	
	static SFHCoreClientProxy prox = new SFHCoreClientProxy();
	
	public static Block registerBlock(Block block, String modid)
    {
        if (block.getRegistryName() == null)
        {
        	block.setUnlocalizedName(block.getUnlocalizedName());
            block.setRegistryName(block.getUnlocalizedName());
        }
        
        RegisterBlocks.blocks.add(block);
        if (block.getRegistryName() == null)
        {
        	System.out.println(block.getUnlocalizedName());
        	block.setRegistryName(block.getUnlocalizedName());
        }
        prox.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
        {
            block.setRegistryName(block.getUnlocalizedName());
        }

        RegisterBlocks.blocks.add(block);
        RegisterTileEntity.registerTileEntity(block);
        
        //itemBlock.registerItems();
        prox.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);

        return block;
    }
    
    public static Item registerItem(Item item, String modid)
    {
        if (item.getRegistryName() == null)
        {
        	item.setUnlocalizedName(item.getUnlocalizedName());
            item.setRegistryName(item.getUnlocalizedName());
        }
        RegisterItems.items.add(item);
        prox.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, int no, String modid)
    {
        if (item.getRegistryName() == null)
        {
        	item.setUnlocalizedName(item.getUnlocalizedName());
            item.setRegistryName(item.getUnlocalizedName());
        }
        RegisterItems.items.add(item);
        prox.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
}
