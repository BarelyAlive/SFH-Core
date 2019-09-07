package mod.sfhcore.blocks.tiles;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.sfhcore.fluid.FluidTankSingle;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;

public class TileFluidInventory extends TileInventory
{
	private FluidTankSingle tank;

	public FluidTank getTank()
	{
		return tank;
	}

	public TileFluidInventory(final int invSize, final String machineCustomName, final FluidTankSingle tank) {
		super(invSize, machineCustomName);
		this.tank = tank;
	}

	@Override
	public void update() {}

	public int emptyRoom()
	{
		int a = tank.getFluidAmount();
		return tank.getCapacity() - a;
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbt) {
		tank.writeToNBT(nbt);
		return super.writeToNBT(nbt);
	}

	@Override
	public boolean hasCapability(@Nonnull final Capability<?> capability, @Nullable final EnumFacing facing)
	{
		return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
	}

	@Override
	@Nullable
	public <T> T getCapability(@Nonnull final Capability<T> capability, @Nullable final EnumFacing facing)
	{
		if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
			return (T) tank;
		return super.getCapability(capability, facing);
	}

	@Override
	public void setField(final int id, final int value) {}

	@Override
	public int getField(final int id)
	{
		return id;
	}

	@Override
	public int getFieldCount() {
		return super.getFieldCount() + 1;
	}
}