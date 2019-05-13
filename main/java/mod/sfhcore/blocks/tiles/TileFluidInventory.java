package mod.sfhcore.blocks.tiles;

import java.util.List;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.sfhcore.util.TankUtil;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import scala.Int;

public class TileFluidInventory extends TileInventory
{	
	private static List<Fluid> acceptedFluids;
	private FluidTank tank;
	
	public FluidTank getTank() {
		return tank;
	}

	private int maxCapacity;
	
	public int getMaxCapacity()
	{
		return maxCapacity;
	}

	public TileFluidInventory(int invSize, String machineCustomName, int maxCapacity) {
		super(invSize, machineCustomName);
		this.tank = new FluidTank(maxCapacity);
		this.maxCapacity = maxCapacity;
	}
	
	public void update() {}
	
	public int emptyRoom()
	{
		int a = tank.getFluidAmount();
		return getMaxCapacity() - a;
	}
	
	public static boolean hasAcceptedFluids(@Nullable Fluid f)
	{
		if(f == null) return false;
		for(Fluid fluid : acceptedFluids) {
			if(f == fluid) return true;
		}
		return false;
	}
	
	public static void setAcceptedFluids(List<Fluid> listf) {
		acceptedFluids = listf;
	}
	
	public static List<Fluid> getAcceptedFluids() {
		return acceptedFluids;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		tank.readFromNBT(nbt);
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		tank.writeToNBT(nbt);
		return super.writeToNBT(nbt);
	}
	
	@Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY || super.hasCapability(capability, facing);
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)
            return (T) tank;
        return super.getCapability(capability, facing);
    }
    
    @Override
    public void setField(int id, int value) {
    	if (id < super.getFieldCount())
    	{
    		super.setField(id, value);
    	}
    	else
    	{
    		if (id == super.getFieldCount())
    		{
    			if (this.tank.getFluid() != null)
    				this.tank.getFluid().amount = value;
    		}
    	}
    }
    
    @Override
    public int getField(int id) {
    	if (id < super.getFieldCount())
    	{
        	return super.getField(id);
    	}
    	else
    	{
    		if (id == super.getFieldCount())
    		{
    			return this.tank.getFluidAmount();
    		}
    	}
    	return 0;
    }
    
    @Override
    public int getFieldCount() {
    	return super.getFieldCount() + 1;
    }
}