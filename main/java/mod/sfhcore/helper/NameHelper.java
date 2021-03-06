package mod.sfhcore.helper;

import java.util.Objects;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class NameHelper
{
	public static String getName(final Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return item.getRegistryName().getResourcePath();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the name of an item, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getName(final Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return block.getRegistryName().getResourcePath();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the name of a block, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getName(final ItemStack stack)
	{
		if(NotNull.checkNotNull(stack))
			try {
				return Objects.requireNonNull(stack.getItem().getRegistryName()).getResourcePath();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the name of an ItemStack, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getModID(final Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return item.getRegistryName().getResourceDomain();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the mod id of an item, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getModID(final Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return block.getRegistryName().getResourceDomain();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the mod id of a block, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getBlockName(final Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return Objects.requireNonNull(Block.getBlockFromItem(item).getRegistryName()).getResourcePath();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the name of an item, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getItemName(final Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return Objects.requireNonNull(Item.getItemFromBlock(block).getRegistryName()).getResourcePath();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the name of a block, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getBlockModID(final Item item)
	{
		if(item != null && item.getRegistryName() != null)
			try {
				return Objects.requireNonNull(Block.getBlockFromItem(item).getRegistryName()).getResourceDomain();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the mod id of an item, but it was null");
				e.printStackTrace();
			}

		return null;
	}

	public static String getItemModID(final Block block)
	{
		if(block != null && block.getRegistryName() != null)
			try {
				return Objects.requireNonNull(Item.getItemFromBlock(block).getRegistryName()).getResourceDomain();
			} catch (final NullPointerException e) {
				LogUtil.fatal("[SFHCore] tried to get the mod id of a block, but it was null");
				e.printStackTrace();
			}

		return null;
	}
}
