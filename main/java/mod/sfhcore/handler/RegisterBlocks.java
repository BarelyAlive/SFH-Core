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
		String unlocalizedName;
		for(int i = 0; i < blocks.size(); i++)
		{
        	unlocalizedName = blocks.get(i).getUnlocalizedName();
        	while (unlocalizedName.startsWith("tile."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.startsWith("item."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
			registry.register(new ItemBlock(blocks.get(i)).setUnlocalizedName(unlocalizedName).setRegistryName("nethertweaksmod", unlocalizedName));
		}
	}
	
	public static void registerModels()
	{
		String unlocalizedName;
		for(int i = 0; i < blocks.size(); i++)
		{
        	unlocalizedName = blocks.get(i).getUnlocalizedName();
        	while (unlocalizedName.startsWith("tile."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
        	while (unlocalizedName.startsWith("item."))
        	{
        		unlocalizedName = unlocalizedName.substring(5);
        	}
			((SFHCoreClientProxy)SFHCore.proxy).tryHandleBlockModel(blocks.get(i), unlocalizedName, "nethertweaksmod");
		}
	}
	
}
