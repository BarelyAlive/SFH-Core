package mod.sfhcore.items;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CustomItem extends Item implements IVariantProvider{
	
	private int sub;
		
	public CustomItem(Item container, int maxstack, CreativeTabs tab, boolean subtypes, int subnumber, String name){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setUnlocalizedName(name);
		this.setHasSubtypes(subtypes);
		this.sub = subnumber;
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < sub; i ++) {
	        items.add(items.get(i));
	    }
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}

	@Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < sub; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + i));
        return ret;
    }
}
