package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterBlocks {
	
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void register(IForgeRegistry<Block> registry)
	{
		for(Block block : blocks)
		{
			if (block != null && block.getRegistryName() != null)
			{
				registry.register(block);
			}
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		ResourceLocation loc;
		for(Block block : blocks)
		{
			if (block != null && block.getRegistryName() != null)
			{
				loc = block.getRegistryName();
				Item item = new ItemBlock(block);
				item.setRegistryName(loc.getResourceDomain(), "item_" + loc.getResourcePath());
				
				registry.register(item);
			}
		}
			
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(Block block : blocks)
		{
			if (block != null && block.getRegistryName() != null)
			{
	        	loc = block.getRegistryName();
				((SFHCoreClientProxy)SFHCore.proxy).tryHandleBlockModel(block, loc);
			}
		}
	}
	
}
