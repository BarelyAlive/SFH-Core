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
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import scala.Int;

public class TileEntityFluidBase extends TileEntityBase {
	
	private static List<Fluid> acceptedFluids;
	
	public static int MAX_CAPACITY;
	int volume;
	public FluidStack fluid;
	public FluidTank tank = new FluidTank(fluid, (int) volume);
	
	public TileEntityFluidBase(int invSize, String machineCustomName, int MAX_CAPACITY) {
		super(invSize, machineCustomName);
		volume = 0;
		fluid = new FluidStack(FluidRegistry.WATER, 0);
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
		super.readFromNBT(nbt);
		this.fluid.amount = nbt.getShort("fluid");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		nbt.setShort("fluid", ((short) this.fluid.amount));
		return nbt;
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
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
		// TODO Auto-generated method stub
		return new FluidStack(FluidRegistry.WATER, Int.MaxValue());
	}

	@Override
	public IFluidTankProperties[] getTankProperties() {
		IFluidTankProperties[] prop = new IFluidTankProperties[fluid.amount];
		return prop;
	}
	
}