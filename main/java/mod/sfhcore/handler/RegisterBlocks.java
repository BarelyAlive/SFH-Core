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
			if (!block.equals(blocks.get((blocks.size() - 1))))
			{
				registry.register(block);
			}
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		for(Block block : blocks)
		{
			if (!block.equals(blocks.get((blocks.size() - 1))))
			{
				Item item = new ItemBlock(block).setRegistryName(block.getRegistryName());
				registry.register(item);
			}
		}
			
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(Block block : blocks)
		{
			if (!block.equals(blocks.get((blocks.size() - 1))))
			{
	        	loc = block.getRegistryName();
				((SFHCoreClientProxy)SFHCore.proxy).tryHandleBlockModel(block, loc);
			}
		}
	}
	
}
