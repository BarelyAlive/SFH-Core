package mod.sfhcore.registries;

import mod.sfhcore.helper.NameHelper;
import mod.sfhcore.helper.NotNull;
import mod.sfhcore.util.LogUtil;
import mod.sfhcore.vars.TE2Block;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;

public class Registry extends NameHelper
{
	//This is the class with the methods to register your stuff
	
	public static Block registerBlock(Block block)
    {
		if (NotNull.checkNotNull(block))
		{
			for(Block b : RegisterBlocks.getBlocks())
				if(b.equals(block))
				{
					LogUtil.warn("SFHCORE: Can't register " + block.getRegistryName() + ", because it's already registered!");
					return block;
				}
			
			block.setUnlocalizedName(getName(block));
			
			RegisterBlocks.getBlocks().add(block);
			
			//Create ItemBlock
			Item item = new ItemBlock(block);
			
			item.setUnlocalizedName("item_" + getName(block));
			item.setRegistryName(getModID(block), "item_" + getName(block));
			
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
					LogUtil.warn("SFHCORE: Can't register " + itemBlock.getRegistryName() + ", because it's already registered!");
					return itemBlock;
				}
    		
    		itemBlock.setUnlocalizedName("item_" + getName(itemBlock));
    		
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
					LogUtil.warn("SFHCORE: Can't register " + item.getRegistryName() + ", because it's already registered!");
					return item;
				}
    		item.setUnlocalizedName("item_" + getName(item));
    		
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
					LogUtil.warn("SFHCORE: Can't register " + chant.getRegistryName() + ", because it's already registered!");
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
					LogUtil.warn("SFHCORE: Can't register " + te.getName() + ", because it's already registered!");
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
