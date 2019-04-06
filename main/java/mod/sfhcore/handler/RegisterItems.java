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
		for(int i = 0; i < items.size(); i++)
		{
			registry.register(items.get(i));
		}
	}
	
	public static void registerModels()
	{
		ResourceLocation loc;
		for(int i = 0; i < items.size(); i++)
		{
        	loc = items.get(i).getRegistryName();
        	
	        ((SFHCoreClientProxy)SFHCore.proxy).tryHandleItemModel(items.get(i), loc);
		}
	}
	
}
