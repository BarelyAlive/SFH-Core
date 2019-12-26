package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nonnull;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.Config;
import mod.sfhcore.util.ItemInfo;
import mod.sfhcore.util.LogUtil;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.furnace.FurnaceFuelBurnTimeEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ModFuelHandler
{
	public static final List<Pair<ItemStack, Integer>> FUEL = new ArrayList<>();

	@SubscribeEvent
	public int getBurnTime(final FurnaceFuelBurnTimeEvent e)
	{
		int burnTime = -1;
		final ItemStack stack = e.getItemStack();

		//have to do this to prevent crashes
		if (stack.isEmpty() || stack.getItem().getRegistryName() == null)
		{
			e.setBurnTime(burnTime);
			return burnTime;
		}
		
		final Item item = stack.getItem();
		
		//Try FUEL list
		for(final Pair<ItemStack, Integer> fuel : FUEL)
			if(ItemStack.areItemsEqual(stack, fuel.getLeft()))
			{
				e.setBurnTime(fuel.getRight());
				return fuel.getRight();
			}
		
		//Buckets
		final FluidStack fs = FluidUtil.getFluidContained(stack);
		final IFluidHandlerItem ifhi = FluidUtil.getFluidHandler(stack);
		
		if(Config.useAllHotFluidContainer && fs != null && ifhi != null)
		{
			final Fluid f = fs.getFluid();
			
			if(f.getTemperature() >= FluidRegistry.LAVA.getTemperature() && fs.amount == 1000)
				//Stelle (hoffentlich) sicher dass es ein Bucket ist
				if(ifhi.fill(new FluidStack(f, 1), false) == 0 &&
						ifhi.drain(new FluidStack(f, 999), false) == null)
					if(ifhi.drain(1000, true) != null)
					{
						final int temp = (int)(20000 * ((float)f.getTemperature() / (float)FluidRegistry.LAVA.getTemperature()));
						
						e.setBurnTime(temp);
						return temp;
					}
		}

		//Try to get the built-in burntime
		try {
			burnTime = item.getItemBurnTime(stack);
		} catch (final NullPointerException ex) {
			LogUtil.fatal("[SFHCore] tried to get the burn time of " + item.getRegistryName() + " and it was NULL!");
		}
		
		//ENDE
		burnTime = Math.max(burnTime, -1);

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
		if(time < 1)
		{
			LogUtil.warn("[SFHCore] can't add " + item.getItem().getRegistryName() + " with a burntime of " + time + " ticks!");
			return false;
		}

		return FUEL.add(new ImmutablePair<>(item.getItemStack(), time));
	}
}