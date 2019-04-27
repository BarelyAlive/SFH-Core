package mod.sfhcore.tileentities;

import java.util.List;

import javax.annotation.Nullable;

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
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import scala.Int;

public class TEBaseFluidInventory extends TEBaseInventory implements IFluidHandler, IFluidTank {
	
	private static List<Fluid> acceptedFluids;
	
	public static int MAX_CAPACITY;
	public FluidStack tank; // = new FluidStack(FluidRegistry.WATER, 0);			//new FluidTank(fluid, (int) volume);
	
	public TEBaseFluidInventory(int invSize, String machineCustomName, int MAX_CAPACITY) {
		super(invSize, machineCustomName);
		this.tank = new FluidStack(FluidRegistry.WATER, 0);
		this.MAX_CAPACITY = MAX_CAPACITY;
	}
	
	public void update() {
	}
	
	public static boolean hasAcceptedFluids(Fluid f) {
		for(Fluid fluid : acceptedFluids) {
			if(fluid == f) return true;
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
		this.tank.amount = nbt.getShort("fluid");
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("fluid", ((short) this.tank.amount));
		return super.writeToNBT(nbt);
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		for(Fluid f : acceptedFluids) {
			if(resource.getFluid() != f){
				return 0;
			}
			else if (resource.amount <= (this.MAX_CAPACITY - this.tank.amount)) {
				if (doFill)
				{
					this.tank = new FluidStack(resource.getFluid(), (this.tank.amount + resource.amount));
				}
				return resource.amount;
			}
			else {
				int empty_space_tank = (this.MAX_CAPACITY - this.tank.amount);
				int oldAmount = resource.amount;
				resource.amount = empty_space_tank;
				if (doFill)
				{
					this.tank = new FluidStack(resource.getFluid(), this.tank.amount);
				}
				return (oldAmount - empty_space_tank);
			}
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if (this.tank.getFluid().equals(resource.getFluid()))
		{
			return this.drain(resource.amount, doDrain);
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		Fluid f = this.tank.getFluid();
		if (this.tank.amount >= maxDrain)
		{
			if (doDrain)
			{
				this.tank.amount -= maxDrain;
			}
			return new FluidStack(f, maxDrain);
		}
		else
		{
			return new FluidStack(f, 0);
		}
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] prop = new IFluidTankProperties[1];
		return prop;
	}
	
	@Override
	public int getField(int id) {
		if (id == super.getFieldCount())
		{
			return this.tank.amount;
		}
		return super.getField(id);
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return super.getFieldCount() + 1;
	}
	
	@Override
	public void setField(int id, int value) {
		if (id < super.getFieldCount())
			super.setField(id, value);
		if (id == super.getFieldCount())
		{
			this.tank.amount = value;
		}
	}
	
	@Override
	public FluidStack getFluid() {
		return tank;
	}

	@Override
	public int getFluidAmount() {
		return tank.amount;
	}

	@Override
	public int getCapacity() {
		return MAX_CAPACITY;
	}

	@Override
	public FluidTankInfo getInfo() {
		return new FluidTankInfo(this.tank, this.MAX_CAPACITY);
	}
	
	@Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
        readFromNBT(pkt.getNbtCompound());
    }

    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
    	super.getUpdatePacket();
        NBTTagCompound compound = new NBTTagCompound();        
        writeToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 1, compound);
    }
	
}