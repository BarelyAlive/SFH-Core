package mod.sfhcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.handler.CustomFuelHandler;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CustomItem extends Item
{
	public CustomItem(final CreativeTabs tab, final ResourceLocation loc){
		this(null, 64, tab, false, 1, loc);
	}

	public CustomItem(final int maxstack, final CreativeTabs tab, final ResourceLocation loc){
		this(null, maxstack, tab, false, 1, loc);
	}

	public CustomItem(final Item container, final int maxstack, final CreativeTabs tab, final boolean subtypes, final int subnumber, final ResourceLocation loc){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setRegistryName(loc);
		setUnlocalizedName(loc.getResourcePath());
	}

	@Override
	public int getItemBurnTime(final ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.getFuelList())
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
				return f.getRight();
		return 0;
	}
}
