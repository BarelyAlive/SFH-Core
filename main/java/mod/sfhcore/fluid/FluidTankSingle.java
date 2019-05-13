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

	public FluidTankSingle(Fluid fluid, int amount, int capacity) {
		super(fluid, amount, capacity);
		this.f = fluid;
	}
	
	@Override
	public boolean canFillFluidType(FluidStack fluid)
	{
		return fluid.getFluid() == f;
	}
}
