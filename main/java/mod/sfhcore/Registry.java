package mod.sfhcore;

import mod.sfhcore.handler.RegisterBlocks;
import mod.sfhcore.handler.RegisterItems;
import mod.sfhcore.handler.RegisterTileEntity;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class Registry {
	
	static SFHCoreClientProxy prox = new SFHCoreClientProxy();
	
	public static Block registerBlock(ResourceLocation loc)
    {      
        RegisterTileEntity.registerTileEntity(block);
        RegisterBlocks.blocks.add(block);
        
        //SFHCore.proxy.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
        {
        	String unlocalizedName = block.getUnlocalizedName();
        	while (unlocalizedName.startsWith("tile."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.startsWith("item."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.endsWith(".name"))
        	{
        		unlocalizedName = unlocalizedName.substring((unlocalizedName.length() - 5), unlocalizedName.length());
        	}
        	block.setUnlocalizedName(unlocalizedName);
            block.setRegistryName(unlocalizedName);
        }

        RegisterTileEntity.registerTileEntity(block);
        RegisterBlocks.blocks.add(block);
        
        //itemBlock.registerItems();
       //SFHCore.proxy.tryHandleBlockModel(block, block.getUnlocalizedName(), modid);

        return block;
    }
    
    public static Item registerItem(Item item, String modid)
    {
        if (item.getRegistryName() == null)
        {
        	String unlocalizedName = item.getUnlocalizedName();
        	while (unlocalizedName.startsWith("tile."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.startsWith("item."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.endsWith(".name"))
        	{
        		unlocalizedName = unlocalizedName.substring((unlocalizedName.length() - 5), unlocalizedName.length());
        	}
        	item.setUnlocalizedName(unlocalizedName);
            item.setRegistryName(unlocalizedName);
        }
        RegisterItems.items.add(item);
        //SFHCore.proxy.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, int no, String modid)
    {
        if (item.getRegistryName() == null)
        {
        	String unlocalizedName = item.getUnlocalizedName();
        	while (unlocalizedName.startsWith("tile."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.startsWith("item."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	item.setUnlocalizedName(unlocalizedName);
            item.setRegistryName(unlocalizedName);
        }
        RegisterItems.items.add(item);
        //SFHCore.proxy.tryHandleItemModel(item, item.getUnlocalizedName(), modid);
            
        return item;
    }
}
