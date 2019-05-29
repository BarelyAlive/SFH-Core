package mod.sfhcore.helper;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NameHelper
{
	public static String getName(Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return item.getRegistryName().getResourcePath();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the name of an item, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getName(Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return block.getRegistryName().getResourcePath();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the name of a block, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getName(ItemStack stack)
	{
		if(NotNull.checkNotNull(stack))
			try {
				return stack.getItem().getRegistryName().getResourcePath();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the name of an ItemStack, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getModID(Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return item.getRegistryName().getResourceDomain();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the mod id of an item, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getModID(Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return block.getRegistryName().getResourceDomain();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the mod id of a block, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getBlockName(Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return Block.getBlockFromItem(item).getRegistryName().getResourcePath();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the name of an item, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getItemName(Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return Item.getItemFromBlock(block).getRegistryName().getResourcePath();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the name of a block, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getBlockModID(Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return Block.getBlockFromItem(item).getRegistryName().getResourceDomain();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the mod id of an item, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
	
	public static String getItemModID(Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return Item.getItemFromBlock(block).getRegistryName().getResourceDomain();
			} catch (NullPointerException e) {
				LogUtil.fatal("SFHCore tried to get the mod id of a block, but it was null");
				e.printStackTrace();
			}
		
		return null;
	}
}
