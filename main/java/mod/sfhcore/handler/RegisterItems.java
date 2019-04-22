package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterItems {
	
	private static List<Item> items = new ArrayList<Item>();
	
	public static List<Item> getItems() {
		return items;
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		for(Item item : items)
		{
			if (item != null && item.getRegistryName() != null)
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
			if (item != null && item.getRegistryName() != null)
			{
	        	loc = item.getRegistryName();
	        	
		        ((SFHCoreClientProxy)SFHCore.proxy).tryHandleItemModel(item, loc);
			}
		}
	}
	
}
