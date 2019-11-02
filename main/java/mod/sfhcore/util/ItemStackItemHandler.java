package mod.sfhcore.util;

import javax.annotation.Nonnull;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

/**
 * This class wraps an ItemStack in an IItemHandler to allow code to be unified.
 */
public class ItemStackItemHandler implements IItemHandler {
	private ItemStack wrappedStack;

	public ItemStackItemHandler(final ItemStack stack){
		wrappedStack = stack;
	}

	@Override
	public int getSlots() {
		return 1;
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(final int slot) {
		return slot == 0 ? wrappedStack : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(final int slot, @Nonnull final ItemStack stack, final boolean simulate) {
		if(slot != 0)
			return stack.copy();
		final ItemStack returnStack = stack.copy();
		if(ItemUtil.areStacksEquivalent(wrappedStack, returnStack) && wrappedStack.isStackable()){
			final int toAdd = Math.min(returnStack.getCount(), wrappedStack.getMaxStackSize() - wrappedStack.getCount());
			if(!simulate)
				wrappedStack.grow(toAdd);
			returnStack.shrink(toAdd);
		}
		return returnStack.getCount() > 0 ? returnStack : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(final int slot, final int amount, final boolean simulate) {
		if(slot!=0 || amount <= 0)
			return ItemStack.EMPTY;
		final ItemStack returnStack = wrappedStack.copy();
		returnStack.setCount(Math.min(amount, wrappedStack.getCount()));
		if(!simulate)
			wrappedStack.shrink(returnStack.getCount());
		if(wrappedStack.getCount() == 0)
			wrappedStack = ItemStack.EMPTY;
		return returnStack;
	}

	@Override
	public int getSlotLimit(final int slot) {
		return wrappedStack.getMaxStackSize();
	}
}
