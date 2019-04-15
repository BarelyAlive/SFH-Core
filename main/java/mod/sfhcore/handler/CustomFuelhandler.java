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
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomFuelhandler{
	
	public static List<Pair<ItemStack, Integer>> fuelList = new ArrayList<Pair<ItemStack, Integer>>();
	
	/**
	 * Use this method to register your Fuel-Item with it's burn time.
	 * @param fuel
	 * @param time
	 * @return
	 */
	public static boolean addFuelBurnTime(ItemStack stack, int time) {
		return fuelList.add(new ImmutablePair<ItemStack, Integer>(stack, time));
	}
	
}