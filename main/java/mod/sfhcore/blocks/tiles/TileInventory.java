package mod.sfhcore.blocks.tiles;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
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
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

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
	
	public void work()
	{
		workTime++;
	}
	
	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(int workTime) {
		this.workTime = workTime;
	}
	
	protected String machineCustomName;
	protected static int[] SLOTS_TOP = new int[] {0};
	protected static int[] SLOTS_BOTTOM = new int[] {2, 1};
	protected static int[] SLOTS_SIDES = new int[] {1};
	
	public TileInventory(int invSize, String machineCustomName) {
		setCustomInventoryName(machineCustomName);
		this.machineItemStacks = NonNullList.<ItemStack>withSize(invSize, ItemStack.EMPTY);
	}
	
	//Network && NBT
    public void markDirtyClient() {
        markDirty();
        NetworkHandler.sendNBTUpdate(this);
    }

    public void markDirtyChunk() {
        markDirty();
        IBlockState state = getWorld().getBlockState(getPos());
        getWorld().notifyBlockUpdate(getPos(), state, state, 3);
        NetworkHandler.sendNBTUpdate(this);
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        return new SPacketUpdateTileEntity(pos, 0, getUpdateTag());
    }

    @Override
    @Nonnull
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        readFromNBT(pkt.getNbtCompound());
        IBlockState state = world.getBlockState(pos);
        world.notifyBlockUpdate(pos, state, state, 3);
    }
    //ENDE

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
    @Override
    public ItemStack decrStackSize(int index, int count)
    {
        return ItemStackHelper.getAndSplit(this.machineItemStacks, index, count);
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

        if (stack.getCount() > this.getInventoryStackLimit())
        {
            stack.setCount(this.getInventoryStackLimit());
        }

        if (index == 0 && !flag)
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
		if (!(this.world.getTileEntity(this.pos) instanceof TileInventory)) return false;
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
	public int getField(int id)
	{
		return 0;
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
		switch (side) {
			case UP:
	            return SLOTS_TOP;
	        case DOWN:
	            return SLOTS_BOTTOM;
	        default:
	            return SLOTS_SIDES;
		}
	}

	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		int[] valid_slot = this.getSlotsForFace(direction);
		for (int i = 0; i < valid_slot.length; i++)
		{
			if (valid_slot[i] == index)
			{
				return isItemValidForSlot(index, itemStackIn);
			}
		}
		return false;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		int[] valid_slot = this.getSlotsForFace(direction);
		return isItemValidForSlotToExtract(index, stack);
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