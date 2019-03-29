package mod.sfhcore.blocks.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import mod.sfhcore.tileentities.TileEntityBase;

public class ContainerBase extends Container {

	protected BlockPos pos;
	public TileEntityBase tileentity;
	
	protected ContainerBase(TileEntityBase te) {
		this.tileentity = te;
	}
	
	/**
     * returns a list if itemStacks, for each slot.
     */
	@Override
    public NonNullList<ItemStack> getInventory()
    {
        NonNullList<ItemStack> nonnulllist = NonNullList.<ItemStack>create();

        for (int i = 0; i < this.inventorySlots.size(); ++i)
        {
        	if(this.inventorySlots.get(i).getStack() == null) {
        		this.inventorySlots.get(i).putStack(ItemStack.EMPTY);
        	}
            nonnulllist.add(((Slot)this.inventorySlots.get(i)).getStack());
        }

        return nonnulllist;
    }

	@Override
	public boolean canInteractWith(EntityPlayer player) {
		return this.tileentity.isUsableByPlayer(player);
	}

	@Override
	public ItemStack transferStackInSlot(EntityPlayer player, int index) {
		ItemStack itemstack = ItemStack.EMPTY;
		Slot slot = inventorySlots.get(index);

		if (slot != null && slot.getHasStack()) {
			ItemStack itemstack1 = slot.getStack();
			itemstack = itemstack1.copy();

			int containerSlots = inventorySlots.size() - player.inventory.mainInventory.size();

			if (index < containerSlots) {
				if (!this.mergeItemStack(itemstack1, containerSlots, inventorySlots.size(), true)) {
					return ItemStack.EMPTY;
				}
			} else if (!this.mergeItemStack(itemstack1, 0, containerSlots, false)) {
				return ItemStack.EMPTY;
			}

			if (itemstack1.getCount() == 0) {
				slot.putStack(ItemStack.EMPTY);
			} else {
				slot.onSlotChanged();
			}

			if (itemstack1.getCount() == itemstack.getCount()) {
				return ItemStack.EMPTY;
			}

			slot.onTake(player, itemstack1);
		}

		return itemstack;
	}

	@Override
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean flag = false;
		int i = startIndex;

		if (reverseDirection) {
			i = endIndex - 1;
		}

		if (stack.isStackable()) {
			while (stack.getCount() > 0 && (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex)) {
				Slot slot = this.inventorySlots.get(i);
				ItemStack itemstack = slot.getStack();

				if (slot.isItemValid(stack)) {
					if (!itemstack.isEmpty() && itemstack.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == itemstack.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, itemstack)) {
						int j = itemstack.getCount() + stack.getCount();

						if (j <= stack.getMaxStackSize()) {
							stack.setCount(0);
							itemstack.setCount(j);
							slot.onSlotChanged();
							flag = true;
						} else if (itemstack.getCount() < stack.getMaxStackSize()) {
							stack.shrink(stack.getMaxStackSize() - itemstack.getCount());
							itemstack.setCount(stack.getMaxStackSize());
							slot.onSlotChanged();
							flag = true;
						}
					}
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		if (stack.getCount() > 0) {
			if (reverseDirection) {
				i = endIndex - 1;
			} else {
				i = startIndex;
			}

			while (!reverseDirection && i < endIndex || reverseDirection && i >= startIndex) {
				Slot slot1 = (Slot)this.inventorySlots.get(i);
				ItemStack itemstack1 = slot1.getStack();

				// Forge: Make sure to respect isItemValid in the slot.
				if (itemstack1.isEmpty() && slot1.isItemValid(stack)) {
					slot1.putStack(stack.copy());
					slot1.onSlotChanged();
					stack.setCount(0);
					flag = true;
					break;
				}

				if (reverseDirection) {
					--i;
				} else {
					++i;
				}
			}
		}

		return flag;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int slot, int newValue) {
		super.updateProgressBar(slot, newValue);
		
		if(slot == 0) this.tileentity.workTime = newValue;
}

}