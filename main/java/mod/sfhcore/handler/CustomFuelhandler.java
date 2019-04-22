package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.logging.Log;

import mod.sfhcore.util.LogUtil;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.FMLContainer;
import net.minecraftforge.fml.common.FMLModContainer;
import net.minecraftforge.fml.common.ModContainerFactory;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.FMLLaunchHandler;

public class CustomFuelHandler{
	
	public static List<Pair<ItemStack, Integer>> fuelList = new ArrayList<Pair<ItemStack, Integer>>();

	@SubscribeEvent
	public int getBurnTime(FurnaceFuelBurnTimeEvent e)
	{
		ItemStack stack = e.getItemStack();
		Item item = stack.getItem();
		int burnTime = 0;
		//have to do this to prevent crashes
		if (stack.isEmpty()) {
            return 0;
        }
		
		try {
			burnTime = item.getItemBurnTime(stack);
		} catch (NullPointerException ex) {
			LogUtil.fatal("SFHCore tried to get the burn time of " + item.getRegistryName() + " and it was NULL!");
		}
						
		//Don't delete this return. It must stay at the end.
		if(burnTime < 0)
		{
			return 0;
		}
		return burnTime;
	}
	
	/**
	 * Use this method to register your Fuel-Item with it's burn time.
	 * @param fuel
	 * @param time
	 * @return
	 */
	public static boolean addFuelBurnTime(ItemStack stack, int time)
	{
		if(stack.getItem().getRegistryName() ==null) {
			LogUtil.warn("SFHCore tried to add an item which has registry name!");
			return false;
		}
		
		return fuelList.add(new ImmutablePair<ItemStack, Integer>(stack, time));
	}
	
}