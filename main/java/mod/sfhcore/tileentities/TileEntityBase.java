package mod.sfhcore.tileentities;

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

public class TileEntityBase extends TileEntity implements IInventory{
	
	private ItemStack[] inv;
	private String field_145958_o;
	public static final int maxworkTime = 100;
	public int workTime;
	public static final int MAX_CAPACITY = 16000;
	float volume;
	public FluidStack fluid;
	public FluidTank tank = new FluidTank(fluid, (int) volume);
	World world;
	Block bl;
	EntityPlayer player;
	
	public TileEntityBase() {
		inv = new ItemStack[2];
		workTime = 0;
		volume = 0;
		fluid = new FluidStack(FluidRegistry.WATER, 0);
	}
	
	/**
     * Freezer is freezing
     */
    public boolean isWorking()
    {
        return this.workTime > 0;
    }
	
	public void update() {
	}
	
	private boolean canFreeze()
    {
        if (this.fluid.amount < 1000)
        {
            return false;
        }
        else
        {
            ItemStack itemstack = new ItemStack(Blocks.ICE);
            if (this.inv[0] == null) return true;
            if (!this.inv[0].isItemEqual(itemstack)) return false;
            int result = inv[0].getCount() + itemstack.getCount();
            return result <= getInventoryStackLimit() && result <= this.inv[0].getMaxStackSize(); //Forge BugFix: Make it respect stack sizes properly.
        }
    }

    public void freezeItem()
    {
        if (this.canFreeze())
        {
        	ItemStack itemstack = new ItemStack(Blocks.ICE);

            if (this.inv[0] == null)
            {
                this.inv[0] = itemstack.copy();
            }
            else if (this.inv[0].getItem() == itemstack.getItem())
            {
                this.inv[0].setCount(this.inv[0].getCount() + itemstack.getCount());; // Forge BugFix: Results may have multiple items
            }
        }
        
    }
    
	@Override
	public int getSizeInventory() {
		return inv.length;
	}
	
	@Override
	public ItemStack getStackInSlot(int slot) {
		return inv[slot];
	}
	
	@Override
	public ItemStack decrStackSize(int slot, int amt) {
		ItemStack stack = getStackInSlot(slot);
		if(stack != null) {
			if(stack.getCount() <= amt) {
				setInventorySlotContents(slot, null);
			}
			else {
				stack = stack.splitStack(amt);
				if(stack.getCount() == 0) {
					setInventorySlotContents(slot, null);
				}
			}
		}
		return stack;
	}

	@Override
	public void setInventorySlotContents(int slot, ItemStack stack) {
		inv[slot] = stack;
		if(stack != null && stack.getCount() >= getInventoryStackLimit()) {
			stack.setCount(getInventoryStackLimit());
		}
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		return world.getTileEntity(pos) == this && player.getDistanceSq(pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d) < 64;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
		super.readFromNBT(nbt);
		
		NBTTagList tagList = (NBTTagList) nbt.getTag("Inventory");
		for(int i = 0; i < tagList.tagCount(); i++) {
			NBTTagCompound tag = (NBTTagCompound) tagList.getCompoundTagAt(i);
			byte slot = tag.getByte("Slot");
			if(slot >= 0 && slot < inv.length) {
				inv[slot] = ItemStack.loadItemStackFromNBT(tag);
			}
		}
		this.workTime = nbt.getShort("workTime");
		this.fluid.amount = nbt.getShort("fluid");
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		super.writeToNBT(nbt);
		
		NBTTagList itemList = new NBTTagList();
		for(int i = 0; i < inv.length; i++) {
			ItemStack stack = inv[i];
			if(stack != null) {
				NBTTagCompound tag = new NBTTagCompound();
				tag.setByte("Slot", (byte) i);
				stack.writeToNBT(tag);
				itemList.appendTag(tag);
			}
		}
		nbt.setTag("Inventory", itemList);
		nbt.setShort("workTime", (short)this.workTime);
		return nbt;
	}

	@Override
	public boolean isItemValidForSlot(int slot, ItemStack itemstack) {
		return false;
	}

	public int getWorkTimeRemainingScaled(int i) {
		return this.workTime * i / this.maxworkTime;
	}
	
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasCustomName() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public ITextComponent getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if(resource.getFluid() == FluidRegistry.WATER)
		return new FluidStack(FluidRegistry.WATER, Int.MaxValue());
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

	@Override
	public ItemStack removeStackFromSlot(int index) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void openInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void closeInventory(EntityPlayer player) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getField(int id) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int getFieldCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void clear() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}
	
}