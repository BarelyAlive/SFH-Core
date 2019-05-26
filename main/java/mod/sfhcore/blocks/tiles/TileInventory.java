package mod.sfhcore.blocks.tiles;

import net.minecraft.block.BlockHopper;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.sfhcore.network.NetworkHandler;

public class TileInventory extends TileBase implements ISidedInventory, ITickable, IInteractionObject
{
	private NonNullList<ItemStack> machineItemStacks;
	private int maxworkTime = 0;
	
	public int getMaxworkTime() {
		return maxworkTime;
	}

	public void setMaxworkTime(int maxworkTime) {
		this.maxworkTime = maxworkTime;
	}

	private int workTime = 0;
	
	public void work() {
		workTime++;
	}
	
	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}
	
	protected String machineCustomName;
	
	public TileInventory(int invSize, String machineCustomName) {
		this.setCustomInventoryName(machineCustomName);
		this.machineItemStacks = NonNullList.<ItemStack>withSize(invSize, ItemStack.EMPTY);
	}

    /**
     * Machine is working
     */
    public boolean isWorking()
    {
        return this.getWorkTime() > 0;
    }
	
    @SideOnly(Side.CLIENT)
    public static boolean isWorking(TileInventory inventory)
    {
        return inventory.getWorkTime() > 0;
    }
	
	public void update() {}
	
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
            if(!itemstack.isEmpty())
                return false;

        return true;
    }

    /**
     * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
     */
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.machineItemStacks, index, count);
    }
    
    public void setCustomInventoryName(String name)
    {
        this.machineCustomName = name;
    }

    /**
     * Removes a stack from the given slot and returns it.
     */
    @Override
    public ItemStack removeStackFromSlot(int index)
    {
        return ItemStackHelper.getAndRemove(this.machineItemStacks, index);
    }
	
    /**
     * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
     */
    @Override
    public void setInventorySlotContents(int index, ItemStack stack)
    {
    	ItemStack itemstack = this.machineItemStacks.get(index);
        boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack) && ItemStack.areItemStackTagsEqual(stack, itemstack);
        this.machineItemStacks.set(index, stack);

        if(stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if(index == 0 && !flag)
        {
            this.setWorkTime(0);
            this.markDirty();
        }
    }
    
    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName()
    {
        return machineCustomName;
    }

    /**
     * Returns true if this thing is named
     */
    @Override
    public boolean hasCustomName()
    {
        return this.machineCustomName != null && !this.machineCustomName.isEmpty();
    }

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}
	
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if(!(this.world.getTileEntity(this.pos) instanceof TileInventory)) return false;
        return player.getDistanceSq(
        		(double)this.pos.getX() + 0.5D,
        		(double)this.pos.getY() + 0.5D,
        		(double)this.pos.getZ() + 0.5D) <= 64.0D;
	}
	
	//Networking & NBT
	
	@Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        ItemStackHelper.loadAllItems(nbt, this.machineItemStacks);
		this.setWorkTime(nbt.getShort("workTime"));
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, this.machineItemStacks);
		nbt.setShort("workTime", (short)this.getWorkTime());
		return super.writeToNBT(nbt);
	}
	
	//ggggg

	public int getWorkTimeRemainingScaled(int i) {
		return this.getWorkTime() * i / this.getMaxworkTime();
	}

	@Override
	public ItemStack getStackInSlot(int index) {
		return this.machineItemStacks.get(index);
	}

	@Override
	public void openInventory(EntityPlayer player) {}

	@Override
	public void closeInventory(EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		return true;
	}

	public boolean isItemValidForSlotToExtract(int index, ItemStack itemStack) {
		return true;
	}

	@Override
	public int getField(int id){
		return id;
	}

	@Override
	public void setField(int id, int value) {}

	@Override
	public int getFieldCount() {
		return 0;
	}

	@Override
	public void clear() {
		this.machineItemStacks.clear();
	}
	
	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		int slots = getSizeInventory();
		int[] slotsArr = new int[slots];
		switch (side) {
			case UP:
	            return slotsArr;
	        case DOWN:
	            return slotsArr;
	        default:
	            return slotsArr;
		}
	}
	
	public void extractFromInventory(BlockPos pos, EnumFacing facing)
	{
		TileEntity te = this.getWorld().getTileEntity(pos);
		ItemStack stack= ItemStack.EMPTY;
		IInventory inventory = null;
		if(te == null) return;
		if(!(te instanceof IInventory)) return;
		inventory = ((IInventory)te);
		
		for (int i = 0; i < inventory.getSizeInventory(); i++)
		{
			stack = inventory.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			for (int j = 0; j < this.getSizeInventory(); j++)
			{
				if(!this.canInsertItem(j, stack, facing)) continue;
				if(!this.canExtractFromInventory(j, stack)) continue;
				ItemStack this_stack = this.getStackInSlot(j);
				if(this_stack.isEmpty())
				{
					this.setInventorySlotContents(j, stack);
					inventory.setInventorySlotContents(i, ItemStack.EMPTY);
					j = this.getSizeInventory();
				}
				else
				{
					if(this_stack.getCount() == this_stack.getMaxStackSize()) continue;
					if(!ItemStack.areItemsEqual(stack, this_stack)) continue;
					this_stack.setCount(this_stack.getCount() + stack.getCount());
					if(this_stack.getCount() > this_stack.getMaxStackSize())
					{
						stack.setCount(this_stack.getCount() - this_stack.getMaxStackSize());
					}
					else
					{
						stack = ItemStack.EMPTY;
					}
					this.setInventorySlotContents(j, this_stack);
					inventory.setInventorySlotContents(i, stack);
					if(stack == ItemStack.EMPTY)
					{
						j = this.getSizeInventory();
					}
				}
			}
		}
	}
	
	public boolean canExtractFromInventory(int index, ItemStack stack)
	{
		return true;
	}

	public void insertToInventory(BlockPos pos, EnumFacing facing)
	{
		TileEntity te = this.getWorld().getTileEntity(pos);
		ItemStack stack= ItemStack.EMPTY;
		IInventory inventory = null;
		if(te == null) return;
		if(!(te instanceof IInventory)) return;
		inventory = ((IInventory)te);
		
		for (int i = 0; i < this.getSizeInventory(); i++)
		{
			stack = this.getStackInSlot(i);
			if(stack.isEmpty()) continue;
			if(!isItemValidForSlotToExtract(i, stack)) continue;
			if(!canInsertToInventory(i, stack)) continue;
			for (int j = 0; j < inventory.getSizeInventory(); j++)
			{
				ItemStack inventory_stack = inventory.getStackInSlot(j);
				if(!inventory.isItemValidForSlot(j, stack)) continue;
				if(inventory_stack.isEmpty())
				{
					inventory.setInventorySlotContents(j, stack);
					this.setInventorySlotContents(i, ItemStack.EMPTY);
					j = inventory.getSizeInventory();
				}
				else
				{
					if(inventory_stack.getCount() == inventory_stack.getMaxStackSize()) continue;
					if(!ItemStack.areItemsEqual(stack, inventory_stack)) continue;
					inventory_stack.setCount(inventory_stack.getCount() + stack.getCount());
					if(inventory_stack.getCount() > inventory_stack.getMaxStackSize())
					{
						stack.setCount(inventory_stack.getCount() - inventory_stack.getMaxStackSize());
					}
					else
					{
						stack = ItemStack.EMPTY;
					}
					inventory.setInventorySlotContents(j, inventory_stack);
					this.setInventorySlotContents(i, stack);
					if(stack == ItemStack.EMPTY)
					{
						j = this.getSizeInventory();
					}
				}
			}
		}
	}
	
	public boolean canInsertToInventory(int index, ItemStack stack)
	{
		return true;
	}
	
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		int[] valid_slot = this.getSlotsForFace(direction);
		for (int i = 0; i < valid_slot.length; i++)
			if(valid_slot[i] == index)
				return isItemValidForSlot(index, itemStackIn);
		
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction)
	{
		int[] valid_slot = this.getSlotsForFace(direction);
		for (int i = 0; i < valid_slot.length; i++)
			if(valid_slot[i] == index)
				return isItemValidForSlotToExtract(index, stack);
		
		return false;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return null;
	}
}