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
        RegisterTileEntity.registerTileEntity(block);
        RegisterBlocks.blocks.add(block);
        
        //SFHCore.proxy.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        RegisterTileEntity.registerTileEntity(block);
        RegisterBlocks.blocks.add(block);
        
        //itemBlock.registerItems();
       //SFHCore.proxy.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);

        return block;
    }
    
    public static Item registerItem(Item item, String modid)
    {
        RegisterItems.items.add(item);
        //SFHCore.proxy.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, int no, String modid)
    {
        RegisterItems.items.add(item);
        //SFHCore.proxy.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
}
