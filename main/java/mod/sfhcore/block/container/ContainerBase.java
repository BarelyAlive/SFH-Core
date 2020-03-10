package mod.sfhcore.block.container;

import mod.sfhcore.block.tile.TileInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ContainerBase extends Container {

	protected BlockPos pos;
	public TileInventory tileentity;

	protected ContainerBase(final TileInventory te) {
		tileentity = te;
	}

	/**
	 * returns a list if itemStacks, for each slot.
	 */
	@Override
	public NonNullList<ItemStack> getInventory()
	{
		final NonNullList<ItemStack> nonnulllist = NonNullList.create();

        for (final Slot inventorySlot : inventorySlots) {
            if (inventorySlot.getStack() == null)
                inventorySlot.putStack(ItemStack.EMPTY);
            nonnulllist.add(inventorySlot.getStack());
        }

		return nonnulllist;
	}

	@Override
	public boolean canInteractWith(final EntityPlayer player) {
		return tileentity.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(final EntityPlayer player, final int index) {
		ItemStack previous = ItemStack.EMPTY;
		final Slot slot = inventorySlots.get(index);

		if (slot.getStack() != ItemStack.EMPTY && slot.getHasStack()) {
			final ItemStack current = slot.getStack();
			previous = current.copy();

			if (index < tileentity.getSizeInventory()) {
				if (!mergeItemStack(current, tileentity.getSizeInventory(), 35+tileentity.getSizeInventory(), true))
					return ItemStack.EMPTY;
				slot.onSlotChange(current, previous);
			} else if (!mergeItemStack(current, 0, tileentity.getSizeInventory(), false))
				return ItemStack.EMPTY;

			if (current.getCount() == 0)
				slot.putStack(ItemStack.EMPTY);
			else
				slot.onSlotChanged();

			if (current.getCount() == previous.getCount())
				return ItemStack.EMPTY;
			slot.onTake(player, current);
		}
		return previous;
	}

	@Override
	protected boolean mergeItemStack(final ItemStack stack, final int startIndex, final int endIndex, final boolean reverseDirection) {
		boolean success = false;
		int index = startIndex;

		if (reverseDirection)
			index = endIndex - 1;

		Slot slot;
		ItemStack stackinslot;

		if (stack.isStackable())
			while (stack.getCount() > 0 && (!reverseDirection && index < endIndex || reverseDirection && index >= startIndex)) {
				slot = inventorySlots.get(index);
				stackinslot = slot.getStack();

				if (!stackinslot.isEmpty() && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == stackinslot.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
					final int l = stackinslot.getCount() + stack.getCount();
					final int maxsize = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

					if (l <= maxsize) {
						stack.setCount(0);
						stackinslot.setCount(l);
						slot.onSlotChanged();
						success = true;
					} else if (stackinslot.getCount() < maxsize) {
						stack.setCount( stack.getCount() - (stack.getMaxStackSize() - stackinslot.getCount()));
						stackinslot.setCount(stack.getMaxStackSize());
						slot.onSlotChanged();
						success = true;
					}
				}

				if (reverseDirection)
					--index;
				else
					++index;
			}

		if (stack.getCount() > 0) {
			if (reverseDirection)
				index = endIndex - 1;
			else
				index = startIndex;

			while (!reverseDirection && index < endIndex || reverseDirection && index >= startIndex && stack.getCount() > 0) {
				slot = inventorySlots.get(index);
				stackinslot = slot.getStack();

				if (stackinslot.isEmpty() && slot.isItemValid(stack))
					if (stack.getCount() < slot.getItemStackLimit(stack)) {
						slot.putStack(stack.copy());
						stack.setCount(0);
						success = true;
						break;
					} else {
						final ItemStack newstack = stack.copy();
						newstack.setCount(slot.getItemStackLimit(stack));
						slot.putStack(newstack);
						stack.setCount(stack.getCount() - slot.getItemStackLimit(stack));
						success = true;
					}

				if (reverseDirection)
					--index;
				else
					++index;
			}
		}

		return success;
	}

	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(final int slot, final int newValue) {
		super.updateProgressBar(slot, newValue);
		tileentity.setField(slot, newValue);
	}

}