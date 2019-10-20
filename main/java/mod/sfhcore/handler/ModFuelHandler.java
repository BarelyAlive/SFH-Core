package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.Config;
import mod.sfhcore.util.ItemInfo;
import mod.sfhcore.util.LogUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModFuelHandler{

	public static final List<Pair<ItemStack, Integer>> FUEL = new ArrayList<>();

	@SubscribeEvent
	public int getBurnTime(final FurnaceFuelBurnTimeEvent e)
	{
		int burnTime = 0;
		ItemStack stack = e.getItemStack();

		//have to do this to prevent crashes
		if (stack.isEmpty())
		{
			e.setBurnTime(0);
			return 0;
		}
		
		Item item = stack.getItem();
		
		for(Pair<ItemStack, Integer> fuel : FUEL)
		{
			if(ItemStack.areItemsEqual(stack, fuel.getLeft()))
			{
				e.setBurnTime(fuel.getRight());
				return Math.max(fuel.getRight(), 0);
			}
		}

		try {
			burnTime = item.getItemBurnTime(stack);
		} catch (NullPointerException ex) {
			LogUtil.fatal("[SFHCore] tried to get the burn time of " + item.getRegistryName() + " and it was NULL!");
		}
		
		burnTime = Math.max(burnTime, 0);

		e.setBurnTime(burnTime);
		return burnTime;
	}

	/**
	 * Use this method to register your Fuel-Item with it's burn time.
	 * Has to be called after PreInit, so all Items and Blocks are registered.
	 * Otherwise blocks won't work.
	 * @param fuel
	 * @param time
	 * @return
	 */
	public static boolean addFuelBurnTime(@Nonnull final ItemInfo item, @Nonnull final int time)
	{
		if(item.getItemStack().isEmpty())
		{
			LogUtil.error("[SFHCore] tried to add an Itemstack to the Fuelregistry which was empty!");
			return false;
		}
		if(item.getItem().getRegistryName() == null)
		{
			LogUtil.warn("[SFHCore] tried to add an item to the Fuelregistry which has no registry name!");
			return false;
		}

		return FUEL.add(new ImmutablePair<>(item.getItemStack(), time));
	}
}