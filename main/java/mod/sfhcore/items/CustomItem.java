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
	
	public CustomItem(int maxstack, CreativeTabs tab, ResourceLocation loc){
		this(null, maxstack, tab, false, 1, loc);
	}
		
	public CustomItem(Item container, int maxstack, CreativeTabs tab, boolean subtypes, int subnumber, ResourceLocation loc){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setRegistryName(loc);
		this.setHasSubtypes(subtypes);
		this.sub = subnumber;
		this.name = loc.getResourcePath();
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.getFuelList())
		{
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
			{	
				return f.getRight();	
			}
		}
		return 0;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		if(tab.equals(this.getCreativeTab()))
		{
			for (int i = 0; i < sub; i ++) {
		        items.add(new ItemStack(this, 1, i));
		    }
		}
	}

	public String getName(ItemStack stack) {
		if(this.hasSubtypes) {
			return name + "_" + stack.getItemDamage();
		}
		return name;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return "item." + getName(stack);
	}

	@Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        
        if(sub == 1)
        {
        	ret.add(new ImmutablePair<Integer, String>(0, "inventory"));
        	return ret;
        }
        for (int i = 0; i < sub; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + i));
        return ret;
    }
}
