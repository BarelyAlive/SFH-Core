package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterBlocks {
	
	public static List<Block> blocks = new ArrayList<Block>();
	
	public static void register(IForgeRegistry<Block> registry)
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			registry.registerAll(blocks.get(i));
		}
	}
	
	public static void registerItemBlocks(IForgeRegistry<Item> registry)
	{
		for(int i = 0; i < blocks.size(); i++)
		{
			registry.registerAll(new ItemBlock(blocks.get(i)).setRegistryName(blocks.get(i).getRegistryName()));
		}
	}
	
	public static void registerModels()
	{}
	
}
