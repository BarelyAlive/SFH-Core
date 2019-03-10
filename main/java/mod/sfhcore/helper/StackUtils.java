package mod.sfhcore.helper;

import net.minecraft.item.ItemStack;

public class StackUtils {
	
	public static final ItemStack None = null;
	
	public static void addToStackSize(ItemStack stack, int value) {
		int currentStackSize = stack.getCount();
		currentStackSize += value;
		if (currentStackSize > stack.getMaxStackSize())
		{
			currentStackSize = stack.getMaxStackSize();
		}
		stack.setCount(currentStackSize);
	}
	
	public static void substractFromStackSize(ItemStack stack, int value) {
		int currentStackSize = stack.getCount();
		currentStackSize -= value;
		if (currentStackSize < 0)
		{
			currentStackSize = 0;
		}
		stack.setCount(currentStackSize);
	}
	
	public static int getStackSize(ItemStack stack) {
		if(isEmpty(stack))
			return 0;
		return stack.getCount();
	}
	
	public static void setStackSize(ItemStack stack, int value) {
		stack.setCount(value);
	}
	
	public static boolean isEmpty(ItemStack stack) {
		if(stack == ItemStack.EMPTY)
			return true;
		return false;
	}
	
	public static boolean isEqual(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem().equals(stack2.getItem());
	}
	
	public static boolean isEqualWithMeta(ItemStack stack1, ItemStack stack2) {
		return stack1.getItem().equals(stack2.getItem()) && stack1.getItemDamage() == stack2.getItemDamage();
	}
	
	public static int compareStackSize(ItemStack stack1, ItemStack stack2) {
		return getStackSize(stack1) - getStackSize(stack2);
	}
}
