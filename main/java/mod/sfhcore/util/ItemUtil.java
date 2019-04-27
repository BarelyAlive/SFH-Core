package mod.sfhcore.util;

import javax.annotation.Nullable;

import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class ItemUtil
{	
	/**
	 * Compares Items, Damage, and NBT
	 */
	public static boolean areStacksEquivalent(ItemStack left, ItemStack right)
	{
	    return ItemStack.areItemsEqual(right, left) && ItemStack.areItemStackTagsEqual(left, right);
	}
}
