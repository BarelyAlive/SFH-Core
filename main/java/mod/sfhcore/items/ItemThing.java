package mod.sfhcore.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class ItemThing extends Item{
		
	public ItemThing(Item container, int maxstack, CreativeTabs tab){
		setCreativeTab(tab);
		setMaxStackSize(maxstack);
		setContainerItem(container);
	}
		
}
