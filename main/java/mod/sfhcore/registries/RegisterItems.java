package mod.sfhcore.registries;

import java.util.List;

import mod.sfhcore.SFHCore;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterItems {
	
	private static NonNullList<Item> items = NonNullList.create();
	
	public static List<Item> getItems() {
		return items;
	}

	public static void register(IForgeRegistry<Item> registry)
	{
		for(Item item : items)
		{			
			registry.register(item);
		}
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(Item item : items)
		{			
        	loc = item.getRegistryName();
        	
	        ((SFHCoreClientProxy)SFHCore.proxy).tryHandleItemModel(item, loc);
		}
	}
}
