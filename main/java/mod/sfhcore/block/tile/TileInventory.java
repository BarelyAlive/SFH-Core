package mod.sfhcore.block.tile;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IInteractionObject;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileInventory extends TileBase implements ISidedInventory, ITickable, IInteractionObject
{
	protected NonNullList<ItemStack> machineItemStacks;
	private int maxworkTime = 0;

	private int pullTick = 0;
	private int pushTick = 0;

	public int getMaxworkTime() {
		return maxworkTime;
	}

	public void setMaxworkTime(final int maxworkTime) {
		this.maxworkTime = maxworkTime;
	}

	private int workTime = 0;

	public void work() {
		workTime++;
	}

	public int getWorkTime() {
		return workTime;
	}

	public void setWorkTime(final int workTime) {
		this.workTime = workTime;
	}

	protected String machineCustomName;

	public TileInventory(final int invSize) {
		machineItemStacks = NonNullList.withSize(invSize, new ItemStack(Blocks.AIR));
	}

	/**
	 * Machine is working
	 */
	public boolean isWorking()
	{
		return getWorkTime() > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isWorking(final TileInventory inventory)
	{
		return inventory.getWorkTime() > 0;
	}

	public int getWorkTimeRemainingScaled(final int i) {
		return getWorkTime() * i / getMaxworkTime();
	}

	@Override
	public void update() {}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory()
	{
		return machineItemStacks.size();
	}

	@Override
	public boolean isEmpty()
	{
		for (final ItemStack itemstack : machineItemStacks)
			if(!itemstack.isEmpty())
				return false;

		return true;
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(final int index, final int count)
	{
		return ItemStackHelper.getAndSplit(machineItemStacks, index, count);
	}

	public void setCustomInventoryName(final String name)
	{
		machineCustomName = name;
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(final int index)
	{
		return ItemStackHelper.getAndRemove(machineItemStacks, index);
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(final int index, @Nonnull final ItemStack input)
	{
		machineItemStacks.set(index, input);
		markDirty();
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
		return machineCustomName != null && !machineCustomName.isEmpty();
	}

	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	@Override
	public boolean isUsableByPlayer(final EntityPlayer player) {
		if(!(world.getTileEntity(pos) instanceof TileInventory)) return false;
		return player.getDistanceSq(
				pos.getX() + 0.5D,
				pos.getY() + 0.5D,
				pos.getZ() + 0.5D) <= 64.0D;
	}

	//Networking & NBT

	@Override
	public void handleUpdateTag(final NBTTagCompound tag) {
		super.handleUpdateTag(tag);
	}

	@Override
	public void readFromNBT(final NBTTagCompound nbt) {
		ItemStackHelper.loadAllItems(nbt, machineItemStacks);
		setWorkTime(nbt.getInteger("workTime"));
		setMaxworkTime(nbt.getInteger("maxWorktime"));
		pullTick = nbt.getShort("pullTick");
		pushTick = nbt.getShort("pushTick");
		super.readFromNBT(nbt);
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound nbt){
		ItemStackHelper.saveAllItems(nbt, machineItemStacks);
		nbt.setInteger("workTime", getWorkTime());
		nbt.setInteger("maxWorktime", getMaxworkTime());
		nbt.setShort("pullTick", (short) pullTick);
		nbt.setShort("pushTick", (short) pushTick);
		return super.writeToNBT(nbt);
	}

	//ggggg

	@Override
	public ItemStack getStackInSlot(final int index) {
		return machineItemStacks.get(index);
	}

	@Override
	public void openInventory(final EntityPlayer player) {}

	@Override
	public void closeInventory(final EntityPlayer player) {}

	@Override
	public boolean isItemValidForSlot(final int index, final ItemStack stack) {
		return true;
	}

	public boolean isItemValidForSlotToExtract(final int index, final ItemStack itemStack) {
		return true;
	}

	@Override
	public int getField(final int id){
		if (id == 0) return getWorkTime();
		return 0;
	}

	@Override
	public void setField(final int id, final int value) {
		if (id == 0) setWorkTime(value);
	}

	@Override
	public int getFieldCount() {
		return 1;
	}

	@Override
	public void clear() {
		machineItemStacks.clear();
	}

	@Override
	public int[] getSlotsForFace(final EnumFacing side) {
		final int slots = getSizeInventory();
		final int[] slotsArr = new int[slots];
		switch (side) {
			default:
			return slotsArr;
		}
	}

	public void extractFromInventory(final BlockPos pos, final EnumFacing facing)
	{
		final TileEntity te = getWorld().getTileEntity(pos);
		ItemStack stack;
		IInventory inventory;
		if(te == null) return;
		if(!(te instanceof IInventory)) return;
		inventory = (IInventory)te;

		pullTick++;

		if (pullTick == 20) {
			for (int i = 0; i < inventory.getSizeInventory(); i++) {
				stack = inventory.getStackInSlot(i);
				if (stack.isEmpty())
					continue;
				for (int j = 0; j < getSizeInventory(); j++) {
					if (!canInsertItem(j, stack, facing))
						continue;
					if (!canExtractFromInventory(i, stack))
						continue;
					ItemStack this_stack = getStackInSlot(j).copy();
					if (this_stack.isEmpty()) {
						stack.shrink(1);
						inventory.setInventorySlotContents(i, stack);
						this_stack = stack.copy();
						this_stack.setCount(1);
						setInventorySlotContents(j, this_stack);
						j = getSizeInventory();
					} else {
						if (this_stack.getCount() == this_stack.getMaxStackSize())
							continue;
						if (!ItemStack.areItemsEqual(stack, this_stack))
							continue;
						this_stack.grow(1);
						stack.shrink(1);
						setInventorySlotContents(j, this_stack.copy());
						inventory.setInventorySlotContents(i, stack.copy());
						j = getSizeInventory();
						i = inventory.getSizeInventory();
						break;
					}
				}
			}
			pullTick = 0;
		}
	}

	public boolean canExtractFromInventory(final int index, final ItemStack stack)
	{
		return true;
	}

	public void insertToInventory(final BlockPos pos, final EnumFacing facing)
	{
		final TileEntity te = getWorld().getTileEntity(pos);
		ItemStack stack;
		IInventory inventory;
		if(te == null) return;
		if(!(te instanceof IInventory)) return;
		inventory = (IInventory)te;

		pushTick++;

		if (pushTick == 20) {
			for (int i = 0; i < getSizeInventory(); i++) {
				stack = getStackInSlot(i).copy();
				stack.setCount(1);
				if (stack.isEmpty())
					continue;
				if (!isItemValidForSlotToExtract(i, stack))
					continue;
				if (!canInsertToInventory(i, stack))
					continue;
				for (int j = 0; j < inventory.getSizeInventory(); j++) {
					final ItemStack inventory_stack = inventory.getStackInSlot(j);
					if (!inventory.isItemValidForSlot(j, stack))
						continue;
					if (inventory_stack.isEmpty()) {
						inventory.setInventorySlotContents(j, stack);
						setInventorySlotContents(i, ItemStack.EMPTY);
						j = inventory.getSizeInventory();
					} else {
						if (inventory_stack.getCount() == inventory_stack.getMaxStackSize())
							continue;
						if (!ItemStack.areItemsEqual(stack, inventory_stack))
							continue;
						inventory_stack.setCount(inventory_stack.getCount() + stack.getCount());
						if (inventory_stack.getCount() > inventory_stack.getMaxStackSize())
							stack.setCount(inventory_stack.getCount() - inventory_stack.getMaxStackSize());
						else
							stack = ItemStack.EMPTY;
						inventory.setInventorySlotContents(j, inventory_stack);
						setInventorySlotContents(i, stack);
						if (stack == ItemStack.EMPTY)
							j = getSizeInventory();
					}
				}
			}
			pushTick = 0;
		}
	}

	public boolean canInsertToInventory(final int index, final ItemStack stack)
	{
		return true;
	}

	@Override
	public boolean canInsertItem(final int index, final ItemStack itemStackIn, final EnumFacing direction) {
		final int[] valid_slot = getSlotsForFace(direction);
		for (final int element : valid_slot)
			if(element == index)
				return isItemValidForSlot(index, itemStackIn);

		return false;
	}

	@Override
	public boolean canExtractItem(final int index, final ItemStack stack, final EnumFacing direction)
	{
		final int[] valid_slot = getSlotsForFace(direction);
		for (final int element : valid_slot)
			if(element == index)
				return isItemValidForSlotToExtract(index, stack);

		return false;
	}

	@Override
	public Container createContainer(final InventoryPlayer playerInventory, final EntityPlayer playerIn) {
		return null;
	}

	@Override
	public String getGuiID() {
		return null;
	}
}
