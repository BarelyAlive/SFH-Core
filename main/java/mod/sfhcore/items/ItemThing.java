package mod.sfhcore.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemThing extends Item{
		
	public ItemThing(Item container, int maxstack, CreativeTabs tab, boolean subtypes, int subnumber, String name){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
		setUnlocalizedName(name);
		this.setHasSubtypes(subtypes);
	}

	@Override
	public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
		for (int i = 0; i < items.size(); i ++) {
	        items.add(items.get(i));
	    }
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
	    return this.getUnlocalizedName() + "_" + stack.getItemDamage();
	}
}
