package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class CustomFuelhandler{
	
	List<Pair<String, Integer>> fuelList = new ArrayList<Pair<String, Integer>>();

	@SubscribeEvent
	public int getBurnTime(FurnaceFuelBurnTimeEvent e) {
		
		for(Pair<String, Integer> f : fuelList) {
			if(e.getItemStack().getUnlocalizedName().contains(f.getLeft())){
				
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
	public boolean addFuelBurnTime(Item fuel, int time) {
		return fuelList.add(new ImmutablePair<String, Integer>(fuel.getUnlocalizedName(), time));
	}
	
}