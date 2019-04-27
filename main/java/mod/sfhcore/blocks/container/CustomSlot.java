package mod.sfhcore.blocks.container;

import net.minecraft.entity.player.EntityPlayer;
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
	
	public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, int stacksize) {
		this(inventoryIn, index, xPosition, yPosition, stacksize, true);
	}
	
	public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, boolean canPut) {
		this(inventoryIn, index, xPosition, yPosition, 64, canPut);
	}
	
	public CustomSlot(IInventory inventoryIn, int index, int xPosition, int yPosition, int stacksize, boolean canPut) {
		super(inventoryIn, index, xPosition, yPosition);
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return slots;
	}
	
	@Override
	public boolean isItemValid(ItemStack stack) {
		return canPut;
	}
	
}
