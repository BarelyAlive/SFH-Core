package mod.sfhcore.blocks.tiles;

import java.util.List;

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

public class TileFluidInventory extends TileInventory implements IFluidHandler, IFluidTank {
	
	private static List<Fluid> acceptedFluids;
	protected static FluidStack tank;
	
	public static FluidStack getTank()
	{
		FluidStack t = tank;
		return tank;
	}
	
	private static int MAX_CAPACITY;
	
	public static int getMaxCapacity()
	{
		int cap = MAX_CAPACITY;
		return cap;
	}
	
	public TileFluidInventory(int invSize, String machineCustomName, int MAX_CAPACITY) {
		super(invSize, machineCustomName);
		this.tank = new FluidStack(FluidRegistry.WATER, 0);
		this.MAX_CAPACITY = MAX_CAPACITY;
	}
	
	public void update() {}
	
	public static int fillable()
	{
		int a = tank.amount;
		return getMaxCapacity() - a;
	}
	
	public static boolean hasAcceptedFluids(@Nullable Fluid f)
	{
		if(f == null) return false;
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
		this.tank = new FluidStack(
		FluidRegistry.getFluid(nbt.getString("fluidname")),
		nbt.getShort("fluid"));
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		nbt.setShort("fluid", ((short) this.tank.amount));
		nbt.setString("fluidname", this.getTank().getFluid().getName());
		return super.writeToNBT(nbt);
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if(!hasAcceptedFluids(resource.getFluid()))
			return 0;
		if (resource.amount <= fillable()) {
			if (doFill)
			{
				this.tank = new FluidStack(resource.getFluid(), (this.tank.amount += resource.amount));
			}
		}
		else {
			if (doFill)
			{
				this.tank = new FluidStack(resource.getFluid(), this.tank.amount = this.MAX_CAPACITY);
			}
		}
		return fillable();
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		FluidStack joaNeist = new FluidStack(resource.getFluid(), 0);
		
		if(!this.getTank().isFluidEqual(resource)) return joaNeist;
		if(this.getTank().amount <= 0) return joaNeist;
		
		return drain(resource.amount, doDrain);
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		Fluid f = this.tank.getFluid();
		if (this.getTank().amount >= maxDrain)
		{
			if (doDrain)
				this.tank.amount = this.getTank().amount - maxDrain;
			
			return new FluidStack(f, maxDrain);
		}
		else
		{
			if (doDrain) {
				this.tank.amount = 0;
			}
			return new FluidStack(f, this.getTank().amount);
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
}