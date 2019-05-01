package mod.sfhcore.registries;

import akka.io.Tcp.Register;
import mod.sfhcore.handler.TE2Block;
import mod.sfhcore.helper.NotNull;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry
{
	//This is the class with the methods to register your stuff
	
	public static Block registerBlock(Block block)
    {
		if (NotNull.checkNotNull(block))
		{
			RegisterBlocks.getBlocks().add(block);
			Item item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName().getResourceDomain(),
					"item_" + block.getRegistryName().getResourcePath());
			RegisterItems.getItems().add(item);
		}
		
		return block;
    }

    public static ItemBlock registerBlock(ItemBlock itemBlock)
    {
    	if (NotNull.checkNotNull(itemBlock))
		{
	        RegisterBlocks.getItemblocks().add(itemBlock);
	        RegisterBlocks.getBlocks().add(itemBlock.getBlock());
    	}
        
        return itemBlock;
    }
    
    public static Item registerItem(Item item)
    {
    	if (NotNull.checkNotNull(item))
		{
        	RegisterItems.getItems().add(item);
    	}
            
        return item;
    }
    
    public static Enchantment registerEnchantment(Enchantment chant)
    {
    	if (NotNull.checkNotNull(chant))
		{
        	RegisterEnchantments.getEnchantments().add(chant);
    	}
    	
        return chant;
    }
    
    public static void registerTileEntity(Block b, Class te)
	{		
		if (NotNull.checkNotNull(b) && NotNull.checkNotNull(te)) {
			int i = RegisterTileEntity.getIndexForBlock(b);
			if (i == -1) {
				TE2Block te2block = new TE2Block(b, te);
				RegisterTileEntity.getTile_entitys().add(te2block);
			} 
		}
	}
}
