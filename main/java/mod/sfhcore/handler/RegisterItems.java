package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterItems {
	
	public static List<Item> items = new ArrayList<Item>();
	
	public static void register(IForgeRegistry<Item> registry)
	{
		for(Item item : items)
		{
			if (!item.equals(items.get((items.size() - 1))))
			{
				registry.register(item);
			}
		}
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(Item item : items)
		{
			if (!item.equals(items.get((items.size() - 1))))
			{
	        	loc = item.getRegistryName();
	        	
		        ((SFHCoreClientProxy)SFHCore.proxy).tryHandleItemModel(item, loc);
			}
		}
	}
	
}
