package mod.sfhcore.registries;

import akka.io.Tcp.Register;
import mod.sfhcore.handler.TE2Block;
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
		if(block == null)
		{
			LogUtil.warn("SFHCore tried to register a block which was NULL!");
			return block;
		}
        if (block.getRegistryName() != null)
        {
			RegisterBlocks.getBlocks().add(block);
			
			Item item = new ItemBlock(block);
			item.setRegistryName(block.getRegistryName().getResourceDomain(), "item_" + block.getRegistryName().getResourcePath());
			RegisterItems.getItems().add(item);
		}
        else
        {
        	LogUtil.warn("SFHCore tried to register a block which has a null name!");
        }
		return block;
    }

    public static ItemBlock registerBlock(ItemBlock itemBlock)
    {
    	if(itemBlock == null)
		{
			LogUtil.warn("SFHCore tried to register an itemblock which was NULL!");
			return itemBlock;
		}
    	if (itemBlock.getRegistryName() != null)
    	{
	        RegisterBlocks.getItemblocks().add(itemBlock);
	        RegisterBlocks.getBlocks().add(itemBlock.getBlock());
    	}
    	else
    	{
    		LogUtil.warn("SFHCore tried to register an itemblock which has a null name!");
    	}
        
        return itemBlock;
    }
    
    public static Item registerItem(Item item)
    {
    	if(item == null)
		{
			LogUtil.warn("SFHCore tried to register a item which was NULL!");
			return item;
		}
    	if (item.getRegistryName() != null)
    	{
        	RegisterItems.getItems().add(item);
    	}
    	else
    	{
    		LogUtil.warn("SFHCore tried to register an itemblock which has a null name!");
    	}
            
        return item;
    }
    
    public static Enchantment registerEnchantment(Enchantment chant)
    {
    	if(chant == null)
		{
			LogUtil.warn("SFHCore tried to register an enchantment which was NULL!");
			return chant;
		}
    	if (chant.getRegistryName() != null)
    	{
        	RegisterEnchantments.getEnchantments().add(chant);
    	}
    	else
    	{
    		LogUtil.warn("SFHCore tried to register an enchantment which has a null name!");
    	}        
        return chant;
    }
    
    public static void registerTileEntity(Block b, Class te)
	{	
		try {
			b.getRegistryName();
		} catch (NullPointerException e) {
			LogUtil.fatal("SFHCore tried to register a tile entity, but the corresponding block was NULL!");
			return;
		}
		if(te == null)
		{
			LogUtil.fatal("SFHCore tried to register a tile entity, but it was NULL!");
			return;
		}
		if(b.getRegistryName() == null)
		{
			LogUtil.fatal("SFHCore tried to register a tile entity, but the corresponding block has a NULL name!");
			return;
		}
		
		int i = RegisterTileEntity.getIndexForBlock(b);
		
		if(i == -1)
		{
			TE2Block te2block = new TE2Block(b, te);
			RegisterTileEntity.getTile_entitys().add(te2block);
		}
	}
}
