package mod.sfhcore.registries;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterBlocks {
	
	private static NonNullList<Block> blocks = NonNullList.create();
	private static NonNullList<ItemBlock> itemblocks = NonNullList.create();
	
	public static List<Block> getBlocks() {
		return blocks;
	}

	public static List<ItemBlock> getItemblocks() {
		return itemblocks;
	}

	public static void registerBlocks(IForgeRegistry<Block> registry)
	{		
		for(Block block : blocks)
		{			
			registry.register(block);
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		for(ItemBlock block : itemblocks)
		{						
			registry.register(block);
		}
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(Block block : blocks)
		{			
        	loc = block.getRegistryName();
			((SFHCoreClientProxy)SFHCore.proxy).tryHandleBlockModel(block, loc);
		}
		
		for(ItemBlock block : itemblocks)
		{	
			loc = block.getRegistryName();
			((SFHCoreClientProxy) SFHCore.proxy).tryHandleBlockModel(block, loc);
		}
	}
}
