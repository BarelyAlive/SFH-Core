package mod.sfhcore.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankInfinite extends FluidTank
{
	private final Fluid f;

	public Fluid getF() {
		return f;
	}

	public FluidTankInfinite(final Fluid fluid) {
		super(fluid, Integer.MAX_VALUE, Integer.MAX_VALUE);
		f = fluid;
	}

	@Override
	public boolean canFill() {
		return false;
	}

	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain) {
		return new FluidStack(f, maxDrain);
	}

	@Override
	public FluidStack drain(final FluidStack resource, final boolean doDrain) {
		return resource;
	}

	@Override
	public boolean canFillFluidType(final FluidStack fluid)
	{
		return fluid.getFluid() == f;
	}
}
