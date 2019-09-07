package mod.sfhcore.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankSingle extends FluidTank
{
	private final Fluid f;

	public Fluid getF() {
		return f;
	}

	public FluidTankSingle(final Fluid fluid, final int amount, final int capacity) {
		super(fluid, amount, capacity);
		f = fluid;
	}

	@Override
	public boolean canFillFluidType(final FluidStack fluid)
	{
		return fluid.getFluid() == f;
	}
}
