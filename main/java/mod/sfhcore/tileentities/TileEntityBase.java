package mod.sfhcore.tileentities;

import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
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
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.Int;

public class TileEntityBase extends TileEntity implements ITickable,IInventory{
	
	private NonNullList<ItemStack> machineItemStacks = NonNullList.<ItemStack>withSize(3, ItemStack.EMPTY);
	private String field_145958_o;
	public static final int maxworkTime = 100;
	public int workTime;
	public static final int MAX_CAPACITY = 16000;
	float volume;
	World world = this.getWorld();
	
	public TileEntityBase() {
		workTime = 0;
		volume = 0;
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
    
	/**
     * Returns the number of slots in the inventory.
     */
	@Override
    public int getSizeInventory()
    {
        return this.machineItemStacks.size();
    }
	
    @Override
    public boolean isEmpty()
    {
        for (ItemStack itemstack : this.machineItemStacks)
        {
            if (!itemstack.isEmpty())
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.machineItemStacks, index, count);
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.machineItemStacks, index);
    }
	
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    public void setInventorySlotContents(int index, ItemStack stack)
    {
        ItemStack itemstack = this.machineItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.machineItemStacks.set(index, stack);

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
        {
            this.maxWorkTime = this.getWorkTime(stack);
            this.workTime = 0;
            this.markDirty();
        }
    }

    /**
     * Get the name of this object. For players this returns their username
     */
    public String getName()
    {
        return this.hasCustomName() ? this.machineCustomName : "container.machine";
    }

    /**
     * Returns true if this thing is named
     */
    public boolean hasCustomName()
    {
        return this.machineCustomName != null && !this.machineCustomName.isEmpty();
    }

    public void setCustomInventoryName(String p_145951_1_)
    {
        this.machineCustomName = p_145951_1_;
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

	public int getWorkTimeRemainingScaled(int i) {
		return this.workTime * i / this.maxworkTime;
	}

    @SideOnly(Side.CLIENT)
    public static boolean isWorking(IInventory inventory)
    {
        return inventory.getField(0) > 0;
    }
	
}