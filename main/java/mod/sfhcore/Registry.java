package mod.sfhcore;

import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class Registry {
	
	public static Block registerBlock(Block block, String name, String modid)
    {
        if (block.getRegistryName() == null)
        	block.setUnlocalizedName(name);
            block.setRegistryName(name);
        
	        GameRegistry.register(block);
	        GameRegistry.register(new ItemBlock(block).setRegistryName(name));
	        SFHCore.proxy.tryHandleBlockModel(block, name, modid);
        
        return block;
    }

    public static Block registerBlock(ItemBlock itemBlock, String name, int no, String modid)
    {
        Block block = itemBlock.getBlock();

        if (block.getRegistryName() == null)
        	block.setUnlocalizedName(name);
            block.setRegistryName(name);

            GameRegistry.register(block);
            GameRegistry.register(itemBlock.setRegistryName(name));
            SFHCore.proxy.tryHandleBlockModel(block, name, modid);

        return block;
    }
    
    public static Item registerItem(Item item, String name, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(name);
            item.setRegistryName(name);
            GameRegistry.register(item);
            SFHCore.proxy.tryHandleItemModel(item, name, modid);
            
        return item;
    }
	
	public static Item registerItem(Item item, String name, int no, String modid)
    {
        if (item.getRegistryName() == null)
        	item.setUnlocalizedName(name);
            item.setRegistryName(name);
            GameRegistry.register(item);
            SFHCore.proxy.tryHandleItemModel(item, name, modid);
            
        return item;
    }
}
