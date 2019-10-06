package mod.sfhcore.items;

import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.handler.CustomFuelHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class CustomItem extends Item
{
	public CustomItem(final ResourceLocation loc){
		this(64, loc);
	}

	public CustomItem(final int maxstack, final ResourceLocation loc){
		this(null, maxstack, loc);
	}

	public CustomItem(final Item container, final int maxstack, final ResourceLocation loc){
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setRegistryName(loc);
	}

	@Override
	public int getItemBurnTime(final ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.FUEL)
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
				return f.getRight();
		return 0;
	}
}
