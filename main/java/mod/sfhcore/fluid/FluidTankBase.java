package mod.sfhcore.fluid;

import javax.annotation.Nullable;

import mod.sfhcore.blocks.tiles.TileBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class FluidTankBase extends FluidTank {

	private final TileBase tileEntity;

	public FluidTankBase(final int capacity, final TileBase tileEntity) {
		super(capacity);
		this.tileEntity = tileEntity;
	}

	public FluidTankBase(@Nullable final FluidStack fluidStack, final int capacity, final TileBase tileEntity) {
		super(fluidStack, capacity);
		this.tileEntity = tileEntity;
	}

	public FluidTankBase(final Fluid fluid, final int amount, final int capacity, final TileBase tileEntity) {
		super(fluid, amount, capacity);
		this.tileEntity = tileEntity;
	}

	@Override
	protected void onContentsChanged() {
		// updates the tile entity for the sake of things that detect when contents change (such as comparators)
		tileEntity.markDirtyClient();
	}
}
