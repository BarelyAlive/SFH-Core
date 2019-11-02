package mod.sfhcore.items;

import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.handler.ModFuelHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class CustomItem extends Item
{
	public CustomItem(){
		this(64);
	}

	public CustomItem(final int maxstack){
		this(null, maxstack);
	}

	public CustomItem(final Item container, final int maxstack){
		setMaxStackSize(maxstack);
		setContainerItem(container);
	}

	@Override
	public int getItemBurnTime(final ItemStack itemStack) {
		for(final Pair<ItemStack, Integer> f : ModFuelHandler.FUEL)
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
				return f.getRight();
		return 0;
	}
}
