package mod.sfhcore.helper;

import java.util.Objects;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class NotNull
{	
	public static boolean checkNotNull(final Object o)
	{
		if(o == null) return false;
		
		if(o instanceof Block)
		{
			Block block = (Block) o;
			if(Objects.requireNonNull(block.getRegistryName()).toString().isEmpty())
			{
				LogUtil.warn("SFHCore tried to register a block, which has no name!");
				return false;
			}
		}
		else
			if(o instanceof Item)
			{
				Item item = (Item) o;
				if(item.equals(Items.AIR))
				{
					LogUtil.warn("SFHCore tried to register an item which is NULL!");
					return false;
				}
				if(Objects.requireNonNull(item.getRegistryName()).toString().isEmpty())
				{
					LogUtil.warn("SFHCore tried to register an item, which has no name!");
					return false;
				}
			}
			else
				if(o instanceof ItemStack)
				{
					ItemStack item = (ItemStack) o;
					if(item.isEmpty())
						return false;
                    return !item.getItem().getRegistryName().toString().isEmpty();
				}
				else if(o instanceof TileEntity)
				{
					TileEntity te = (TileEntity) o;
				}
				else
					if(o instanceof Enchantment)
					{
						Enchantment chant = (Enchantment) o;
						if(Objects.requireNonNull(chant.getRegistryName()).toString().isEmpty())
						{
							LogUtil.warn("SFHCore tried to register an enchantment, which has no name!");
							return false;
						}
					}
		return true;
	}
}