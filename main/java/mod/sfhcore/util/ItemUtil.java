package mod.sfhcore.util;

import net.minecraft.item.ItemStack;

public class ItemUtil
{
	/**
	 * Compares Items, Damage, and NBT
	 */
	public static boolean areStacksEquivalent(final ItemStack left, final ItemStack right)
	{
		return ItemStack.areItemsEqual(right, left) && ItemStack.areItemStackTagsEqual(left, right);
	}
}
