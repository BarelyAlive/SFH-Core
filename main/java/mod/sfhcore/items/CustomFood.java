package mod.sfhcore.items;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemFood;
import net.minecraft.util.ResourceLocation;

public class CustomFood extends ItemFood
{
	public CustomFood(int amount, float saturation, boolean isWolfFood, ResourceLocation loc, CreativeTabs tab)
	{
		super(amount, saturation, isWolfFood);
		setRegistryName(loc);
	}

}
