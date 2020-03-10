package mod.sfhcore.block.container;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class CustomSlot extends Slot
{
	private int slots;
	private boolean canPut;

	public int getSlots() {
		return slots;
	}

	public CustomSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition, final int stacksize) {
		this(inventoryIn, index, xPosition, yPosition, stacksize, true);
	}

	public CustomSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition, final boolean canPut) {
		this(inventoryIn, index, xPosition, yPosition, 64, canPut);
	}

	public CustomSlot(final IInventory inventoryIn, final int index, final int xPosition, final int yPosition, final int stacksize, final boolean canPut) {
		super(inventoryIn, index, xPosition, yPosition);
	}

	@Override
	public int getSlotStackLimit()
	{
		return slots;
	}

	@Override
	public boolean isItemValid(final ItemStack stack) {
		return canPut;
	}

}
