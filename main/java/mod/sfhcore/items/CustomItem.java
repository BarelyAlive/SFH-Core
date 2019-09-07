package mod.sfhcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.handler.CustomFuelHandler;
import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CustomItem extends Item implements IVariantProvider{

	private int sub;
	private String name;

	public CustomItem(final int maxstack, final CreativeTabs tab, final ResourceLocation loc){
		this(null, maxstack, tab, false, 1, loc);
	}

	public CustomItem(final Item container, final int maxstack, final CreativeTabs tab, final boolean subtypes, final int subnumber, final ResourceLocation loc){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setRegistryName(loc);
		setHasSubtypes(subtypes);
		sub = subnumber;
		name = loc.getResourcePath();
	}

	@Override
	public int getItemBurnTime(final ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.getFuelList())
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
				return f.getRight();
		return 0;
	}

	@Override
	public void getSubItems(final CreativeTabs tab, final NonNullList<ItemStack> items) {
		if(tab.equals(getCreativeTab()))
			for (int i = 0; i < sub; i ++)
				items.add(new ItemStack(this, 1, i));
	}

	public String getName(final ItemStack stack) {
		if(hasSubtypes)
			return name + "_" + stack.getItemDamage();
		return name;
	}

	@Override
	public String getUnlocalizedName(final ItemStack stack) {
		return "item." + getName(stack);
	}

	@Override
	public List<Pair<Integer, String>> getVariants()
	{
		List<Pair<Integer, String>> ret = new ArrayList<>();

		if(sub == 1)
		{
			ret.add(new ImmutablePair<>(0, "inventory"));
			return ret;
		}
		for (int i = 0; i < sub; i++)
			ret.add(new ImmutablePair<>(i, "type=" + i));
		return ret;
	}
}
