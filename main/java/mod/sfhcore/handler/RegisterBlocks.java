package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterBlocks {
	
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void register(IForgeRegistry<Block> registry)
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			//registry.registerAll(blocks.get(i));
			registry.register(blocks.get(i));
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			registry.register(new ItemBlock(blocks.get(i)));
		}
	}
	
	public static void registerModels()
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			((SFHCoreClientProxy)SFHCore.proxy).tryHandleBlockModel(new R);
		}
	}
	
}
