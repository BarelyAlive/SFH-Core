package mod.sfhcore.blocks.container;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IContainerListener;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import mod.sfhcore.tileentities.TEBaseInventory;
import mod.sfhcore.tileentities.TileEntityFluidBase;

public class ContainerBase extends Container {

	protected BlockPos pos;
	public TEBaseInventory tileentity;
	
	protected ContainerBase(TEBaseInventory te) {
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
		ItemStack previous = ItemStack.EMPTY;
	    Slot slot = (Slot) this.inventorySlots.get(index);
	    
	    if (slot.getStack() != ItemStack.EMPTY && slot.getHasStack()) {
	        ItemStack current = slot.getStack();
	        previous = current.copy();

	        if (index < this.tileentity.getSizeInventory()) {
	            if (!this.mergeItemStack(current, this.tileentity.getSizeInventory(), 35+this.tileentity.getSizeInventory(), true))
	                return ItemStack.EMPTY;
	            slot.onSlotChange(current, previous);
	        } else {
	            if (!this.mergeItemStack(current, 0, this.tileentity.getSizeInventory(), false))
	                return ItemStack.EMPTY;
	        }

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
	protected boolean mergeItemStack(ItemStack stack, int startIndex, int endIndex, boolean reverseDirection) {
		boolean success = false;
	    int index = startIndex;

	    if (reverseDirection)
	        index = endIndex - 1;

	    Slot slot;
	    ItemStack stackinslot;

	    if (stack.isStackable()) {
	        while (stack.getCount() > 0 && (!reverseDirection && index < endIndex || reverseDirection && index >= startIndex)) {
	            slot = (Slot) this.inventorySlots.get(index);
	            stackinslot = slot.getStack();

	            if (!(stackinslot.isEmpty()) && stackinslot.getItem() == stack.getItem() && (!stack.getHasSubtypes() || stack.getMetadata() == stackinslot.getMetadata()) && ItemStack.areItemStackTagsEqual(stack, stackinslot)) {
	                int l = stackinslot.getCount() + stack.getCount();
	                int maxsize = Math.min(stack.getMaxStackSize(), slot.getItemStackLimit(stack));

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

	            if (reverseDirection) {
	                --index;
	            } else {
	                ++index;
	            }
	        }
	    }

	    if (stack.getCount() > 0) {
	        if (reverseDirection) {
	            index = endIndex - 1;
	        } else {
	            index = startIndex;
	        }

	        while (!reverseDirection && index < endIndex || reverseDirection && index >= startIndex && stack.getCount() > 0) {
	            slot = (Slot) this.inventorySlots.get(index);
	            stackinslot = slot.getStack();

	            if (stackinslot.isEmpty() && slot.isItemValid(stack)) {
	                if (stack.getCount() < slot.getItemStackLimit(stack)) {
	                    slot.putStack(stack.copy());
	                    stack.setCount(0);
	                    success = true;
	                    break;
	                } else {
	                    ItemStack newstack = stack.copy();
	                    newstack.setCount(slot.getItemStackLimit(stack));
	                    slot.putStack(newstack);
	                    stack.setCount(stack.getCount() - slot.getItemStackLimit(stack));
	                    success = true;
	                }
	            }

	            if (reverseDirection) {
	                --index;
	            } else {
	                ++index;
	            }
	        }
	    }

	    return success;
	}
	
	@Override
	public void detectAndSendChanges() {
		super.detectAndSendChanges();
		for (int i = 0; i < this.listeners.size(); i++)
		{
			IContainerListener iconlis = (IContainerListener)this.listeners.get(i);
			if (this.tileentity.workTime != this.tileentity.getField(0))
			{
				iconlis.sendWindowProperty(this, 0, this.tileentity.getField(0));
			}
		}
		this.tileentity.workTime = this.tileentity.getField(0);
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public void updateProgressBar(int slot, int newValue) {
		super.updateProgressBar(slot, newValue);
		this.tileentity.setField(slot, newValue);
	}

}