package mod.sfhcore.tileentities;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
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

public class TileEntityFluidBase extends TileEntityBase implements IFluidHandler, IFluidTank {
	
	private static List<Fluid> acceptedFluids;
	
	public static int MAX_CAPACITY;
	protected int mb;
	public FluidStack fluid;
	int volume = 0;
	public FluidStack tank = new FluidStack(FluidRegistry.WATER, 0);			//new FluidTank(fluid, (int) volume);
	
	public TileEntityFluidBase(int invSize, String machineCustomName, int MAX_CAPACITY) {
		super(invSize, machineCustomName);
		this.fluid = new FluidStack(FluidRegistry.WATER, 0);
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
		this.fluid.amount = nbt.getShort("fluid");
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("fluid", ((short) this.fluid.amount));
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
		for(Fluid f : acceptedFluids) {
			if(resource.getFluid() == f){
				return fluid;
			}
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		return new FluidStack(tank.getFluid(), Int.MaxValue());
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] prop = new IFluidTankProperties[fluid.amount];
		return prop;
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
	
}