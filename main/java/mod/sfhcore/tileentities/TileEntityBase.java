package mod.sfhcore.tileentities;

import javax.annotation.Nullable;

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
import net.minecraft.tileentity.TileEntityLockable;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityBase extends TileEntityLockable implements ITickable, ISidedInventory {
	
	public NonNullList<ItemStack> machineItemStacks;
	private String field_145958_o;
	public static int maxworkTime = 100;
	public int workTime;
	private String machineCustomName;
	private static final int[] SLOTS_TOP = new int[] {0};
    private static final int[] SLOTS_BOTTOM = new int[] {2, 1};
    private static final int[] SLOTS_SIDES = new int[] {1};
	
	public TileEntityBase(int invSize, String machineCustomName) {
		this.workTime = 0;
		this.machineCustomName = machineCustomName;
		this.machineItemStacks = NonNullList.<ItemStack>withSize(invSize, ItemStack.EMPTY);
	}
	
	/**
     * Freezer is freezing
     */
    public boolean isWorking()
    {
        return this.workTime > 0;
    }
	
    @SideOnly(Side.CLIENT)
    public static boolean isWorking(IInventory inventory)
    {
        return inventory.getField(0) > 0;
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
            this.workTime = 0;
            this.markDirty();
        }
    }
    
    /**
     * Get the name of this object. For players this returns their username
     */
    @Override
    public String getName()
    {
        return this.hasCustomName() ? this.machineCustomName : "container.machine";
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
		if (this.world.getTileEntity(this.pos) != this)
        {
            return false;
        }
        else
        {
            return player.getDistanceSq((double)this.pos.getX() + 0.5D, (double)this.pos.getY() + 0.5D, (double)this.pos.getZ() + 0.5D) <= 64.0D;
        }
	}
	
	@Override
    public void handleUpdateTag(NBTTagCompound tag) {
        super.handleUpdateTag(tag);
	}
	
	@Override
    public NBTTagCompound getUpdateTag() {
        return writeToNBT(new NBTTagCompound());
	}
	
	@Override
    public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
        this.readFromNBT(pkt.getNbtCompound());
    }

    @Nullable
    @Override
    public SPacketUpdateTileEntity getUpdatePacket() {
        NBTTagCompound compound = new NBTTagCompound();
        this.writeToNBT(compound);
        return new SPacketUpdateTileEntity(getPos(), 1, compound);
    }
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) {
        ItemStackHelper.loadAllItems(nbt, this.machineItemStacks);
		this.workTime = nbt.getShort("workTime");
		super.readFromNBT(nbt);
	}
	
	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
		ItemStackHelper.saveAllItems(nbt, this.machineItemStacks);
		nbt.setShort("workTime", (short)this.workTime);
		return super.writeToNBT(nbt);
	}

	public int getWorkTimeRemainingScaled(int i) {
		return this.workTime * i / this.maxworkTime;
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

	@Override
	public int getField(int id) {
		return id;
	}

	@Override
	public void setField(int id, int value) {
		// TODO Auto-generated method stub
		
	}

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
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public Container createContainer(InventoryPlayer playerInventory, EntityPlayer playerIn) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGuiID() {
		// TODO Auto-generated method stub
		return null;
	}
}