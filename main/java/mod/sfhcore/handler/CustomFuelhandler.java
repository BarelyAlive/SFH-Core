package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomFuelhandler{
	
	protected static List<Pair<ResourceLocation, Integer>> fuelList = new ArrayList<Pair<ResourceLocation, Integer>>();

	@SubscribeEvent
	public int getBurnTime(FurnaceFuelBurnTimeEvent e) {
		
		for(Pair<ResourceLocation, Integer> f : fuelList) {
			if(e.getItemStack().getItem().getRegistryName().equals(f.getLeft())){
				
				return f.getRight();
				
			}
		}
				
		//Don't delete this return. It must stay at the end.
		return 0;
	}
	
	/**
	 * Use this method to register your Fuel-Item with it's burn time.
	 * @param fuel
	 * @param time
	 * @return
	 */
	public static boolean addFuelBurnTime(ResourceLocation loc, int time) {
		return fuelList.add(new ImmutablePair<ResourceLocation, Integer>(loc, time));
	}
	
}