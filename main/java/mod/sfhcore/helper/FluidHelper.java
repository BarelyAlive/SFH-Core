package mod.sfhcore.helper;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class FluidHelper {
	
	public static FluidStack getFluidForFilledItem(Item item) {
		return FluidUtil.getFluidContained(new ItemStack(item));
	}

	public static void switchFluids(ItemStack stack, Fluid fromFluid, Fluid toFluid){
		if(stack != null){
			if(stack.getItem() instanceof IFluidHandler){
				if(isDrainableFilledContainer(stack)){
					IFluidTankProperties[] tankProperties = FluidUtil.getFluidHandler(stack).getTankProperties();
					for (IFluidTankProperties properties : tankProperties) {
						if (properties.canFill()) {
							int amount = FluidUtil.getFluidContained(stack).amount;

							FluidUtil.getFluidHandler(stack).drain(new FluidStack(fromFluid, amount), true);
							FluidUtil.getFluidHandler(stack).fill(new FluidStack(toFluid, amount), true);
						}
					}
					
				}
			}
		}
		

	}
	
	public static ItemStack fillContainer(ItemStack stack, Fluid fluid, int amount){
		if(stack != null){
			if(stack.getItem() instanceof IFluidHandler){
				if(isFillableContainerWithRoom(stack)){
					if(FluidUtil.getFluidContained(stack).getFluid() == fluid){
						FluidUtil.getFluidHandler(stack).fill(new FluidStack(fluid, amount), true);
					}	
				}
			}
		}
		return stack;
	}
	
	public static boolean isFillableContainerWithRoom(ItemStack container) {
		if(container == null){
			return false;
		}
		IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
		if (fluidHandler == null) {
			return false;
		}

		IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
		for (IFluidTankProperties properties : tankProperties) {
			if (properties.canFill() && properties.getCapacity() > 0) {
				FluidStack contents = properties.getContents();
				if (contents == null) {
					return true;
				} else if (contents.amount < properties.getCapacity()) {
					return true;
				}
			}
		}

		return false;
	}
	
	public static boolean isDrainableFilledContainer(ItemStack container) {
		if(container != null){
			return false;
		}
		IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
		if (fluidHandler == null) {
			return false;
		}

		IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
		for (IFluidTankProperties properties : tankProperties) {
			if (!properties.canDrain()) {
				return false;
			}

			FluidStack contents = properties.getContents();
			if (contents == null || contents.amount < properties.getCapacity()) {
				return false;
			}
		}

		return true;
	}
	
	public static boolean isFillableContainerAndEmpty(ItemStack container) {
		IFluidHandler fluidHandler = FluidUtil.getFluidHandler(container);
		if (fluidHandler == null) {
			return false;
		}

		IFluidTankProperties[] tankProperties = fluidHandler.getTankProperties();
		for (IFluidTankProperties properties : tankProperties) {
			if (properties.canFill() && properties.getCapacity() > 0) {
				FluidStack contents = properties.getContents();
				if (contents == null) {
					return true;
				} else if (contents.amount > 0) {
					return false;
				}
			}
		}

		return false;
	}

	public static ItemStack getEmptyContainer(ItemStack container) {
		ItemStack empty = container.copy();
		empty.setCount(1);;
		IFluidHandler fluidHandler = FluidUtil.getFluidHandler(empty);
		if (fluidHandler == null) {
			return null;
		}
		if (fluidHandler.drain(Integer.MAX_VALUE, true) != null) {
			return empty;
		}
		return null;
	}
}