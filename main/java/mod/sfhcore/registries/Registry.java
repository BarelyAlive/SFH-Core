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
			for(Block b : RegisterBlocks.getBlocks())
				if(b.equals(block))
				{
					LogUtil.warn("SFHCORE: Cant't register " + block.getRegistryName() + ", because it's already registered!");
					return block;
				}
			
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
    		for(ItemBlock b : RegisterBlocks.getItemblocks())
				if(b.equals(itemBlock))
				{
					LogUtil.warn("SFHCORE: Cant't register " + itemBlock.getRegistryName() + ", because it's already registered!");
					return itemBlock;
				}
    		
	        RegisterBlocks.getItemblocks().add(itemBlock);
	        RegisterBlocks.getBlocks().add(itemBlock.getBlock());
    	}
        
        return itemBlock;
    }
    
    public static Item registerItem(Item item)
    {
    	if (NotNull.checkNotNull(item))
		{
    		for(Item b : RegisterItems.getItems())
				if(b.equals(item))
				{
					LogUtil.warn("SFHCORE: Cant't register " + item.getRegistryName() + ", because it's already registered!");
					return item;
				}
    		
        	RegisterItems.getItems().add(item);
    	}
            
        return item;
    }
    
    public static Enchantment registerEnchantment(Enchantment chant)
    {
    	if (NotNull.checkNotNull(chant))
		{
    		for(Enchantment b : RegisterEnchantments.getEnchantments())
				if(b.equals(chant))
				{
					LogUtil.warn("SFHCORE: Cant't register " + chant.getRegistryName() + ", because it's already registered!");
					return chant;
				}
    		
        	RegisterEnchantments.getEnchantments().add(chant);
    	}
    	
        return chant;
    }
    
    public static void registerTileEntity(Block b, Class te)
	{		
		if (NotNull.checkNotNull(b) && NotNull.checkNotNull(te))
		{
			for(TE2Block te2 : RegisterTileEntity.getTile_Entitys())
				if(te2.getTe().equals(te.getClass()))
				{
					LogUtil.warn("SFHCORE: Cant't register " + te.getName() + ", because it's already registered!");
					return;
				}
			int i = RegisterTileEntity.getIndexForBlock(b);
			if (i == -1) {
				TE2Block te2block = new TE2Block(b, te);
				RegisterTileEntity.getTile_Entitys().add(te2block);
			} 
		}
	}
}
