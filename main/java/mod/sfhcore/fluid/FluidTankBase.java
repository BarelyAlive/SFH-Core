package mod.sfhcore.fluid;

import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;

import mod.sfhcore.blocks.tiles.TileBase;

public class FluidTankBase extends FluidTank {

    private final TileBase tileEntity;

    public FluidTankBase(int capacity, TileBase tileEntity) {
        super(capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(@Nullable FluidStack fluidStack, int capacity, TileBase tileEntity) {
        super(fluidStack, capacity);
        this.tileEntity = tileEntity;
    }

    public FluidTankBase(Fluid fluid, int amount, int capacity, TileBase tileEntity) {
        super(fluid, amount, capacity);
        this.tileEntity = tileEntity;
    }

    @Override
    protected void onContentsChanged() {
        // updates the tile entity for the sake of things that detect when contents change (such as comparators)
        tileEntity.markDirtyClient();
    }
}
