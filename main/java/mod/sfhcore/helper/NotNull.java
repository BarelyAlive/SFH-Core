package mod.sfhcore.helper;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class NotNull
{
	@SuppressWarnings("unused")
	public static boolean checkNotNull(final Object o)
	{
		if(o == null)
			return false;
		if(o instanceof Block)
		{
			Block block = (Block) o;
			if(block == null)
			{
				LogUtil.warn("SFHCore tried to register a block, which is NULL!");
				return false;
			}
			if(block.getRegistryName().toString().isEmpty())
			{
				LogUtil.warn("SFHCore tried to register a block, which has no name!");
				return false;
			}
		}
		else
			if(o instanceof Item)
			{
				Item item = (Item) o;
				if(item == null || item.equals(Items.AIR))
				{
					LogUtil.warn("SFHCore tried to register an item which is NULL!");
					return false;
				}
				if(item.getRegistryName().toString().isEmpty())
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
					if(item.getItem().getRegistryName().toString().isEmpty())
						return false;
				}
				else
					if(o instanceof ItemBlock)
					{
						ItemBlock itemBlock = (ItemBlock) o;
						if(itemBlock == null)
						{
							LogUtil.warn("SFHCore tried to register an itemblock, which is NULL!");
							return false;
						}
						if(itemBlock.getRegistryName().toString().isEmpty())
						{
							LogUtil.warn("SFHCore tried to register an itemblock, which has no name!");
							return false;
						}
						if(itemBlock.getBlock() == null)
						{
							LogUtil.warn("SFHCore tried to register an itemblock, but the corresponding block is NULL!");
							return false;
						}
						if(itemBlock.getBlock().getRegistryName().toString().isEmpty())
						{
							LogUtil.warn("SFHCore tried to register an itemblock, but the corresponding block has no name!");
							return false;
						}
					}
					else
						if(o instanceof TileEntity)
						{
							TileEntity te = (TileEntity) o;
							if(te == null)
							{
								LogUtil.warn("SFHCore tried to register a tile entity, which is NULL!");
								return false;
							}
						}
						else
							if(o instanceof Enchantment)
							{
								Enchantment chant = (Enchantment) o;
								if(chant == null)
								{
									LogUtil.warn("SFHCore tried to register an enchantment, which is NULL!");
									return false;
								}
								if(chant.getRegistryName().toString().isEmpty())
								{
									LogUtil.warn("SFHCore tried to register an enchantment, which has no name!");
									return false;
								}
							}
		return true;
	}
}