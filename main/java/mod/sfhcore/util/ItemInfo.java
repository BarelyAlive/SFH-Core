package mod.sfhcore.util;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

public class ItemInfo implements StackInfo {

	public static final ItemInfo EMPTY = new ItemInfo(ItemStack.EMPTY);

	@Nonnull
	private final Item item;

	private int meta = 0;

	@Nullable
	private NBTTagCompound nbt = null;

	private boolean isWildcard = false;

	public boolean isWildcard() {
		return isWildcard;
	}

	public ItemInfo(@Nonnull final Item item) {
		this.item = item;
		checkWildcard();
	}

	public ItemInfo(@Nonnull final Item item, final int meta) {
		this.item = item;
		if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
			isWildcard = true;
		else {
			this.meta = meta;
			checkWildcard();
		}
	}

	public ItemInfo(@Nonnull final ItemStack stack) {
		item = stack.getItem();
		meta = stack.getItemDamage();
		nbt = stack.getTagCompound();
		checkWildcard();
	}

	public ItemInfo(@Nonnull final Block block) {
		item = Item.getItemFromBlock(block);
		checkWildcard();
	}

	public ItemInfo(@Nonnull final Block block, final int blockMeta) {
		this(block, blockMeta, null);
	}

	public ItemInfo(@Nonnull final Block block, final int blockMeta, @Nullable final NBTTagCompound tag) {
		this(Item.getItemFromBlock(block), blockMeta, tag);
	}

	public ItemInfo(@Nonnull final Item item, final int meta, @Nullable final NBTTagCompound tag) {
		this.item = item;
		if (tag != null)
			nbt = tag.copy();
		if (this.item == Items.AIR)
			isWildcard = true;
		else if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
			isWildcard = true;
		else {
			this.meta = meta;
			checkWildcard();
		}
	}

	public ItemInfo(@Nonnull final Item item, final int meta, @Nonnull final String tag) {
		this.item = item;
		if (this.item == Items.AIR)
			isWildcard = true;
		else if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
			isWildcard = true;
		else {
			this.meta = meta;
			checkWildcard();
		}
		try {
			nbt = JsonToNBT.getTagFromJson(tag);
		} catch (final NBTException e) {
			LogUtil.error("Could not parse NBTTag: " + tag);
			nbt = new NBTTagCompound();
		}
	}

	public ItemInfo(@Nonnull final String string) {
		if (string.isEmpty() || string.length() < 2) {
			item = Items.AIR;
			isWildcard = true;
			return;
		}
		final String[] split = string.split(":");

		Item item = null;
		int meta = 0;

		switch (split.length) {
		case 1:
			item = Item.getByNameOrId("minecraft:" + split[0]);
			break;
		case 2:
			try {
				meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
				item = Item.getByNameOrId("minecraft:" + split[0]);
			} catch (final NumberFormatException e) {
				isWildcard = true;
				meta = 0;
				item = Item.getByNameOrId(split[0] + ":" + split[1]);
			}
			break;
		case 3:
			try {
				meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
				item = Item.getByNameOrId(split[0] + ":" + split[1]);
			} catch (final NumberFormatException e) {
				meta = 0;
				isWildcard = true;
			}
			break;
		default:
			this.item = Items.AIR;
			isWildcard = true;
			return;
		}

		if (item == null) {
			this.item = Items.AIR;
			isWildcard = true;
		} else {
			this.item = item;
			if (meta == -1 || meta == OreDictionary.WILDCARD_VALUE)
				isWildcard = true;
			else {
				this.meta = meta;
				checkWildcard();
			}
		}
	}

	public ItemInfo(@Nonnull final IBlockState state) {
		item = Item.getItemFromBlock(state.getBlock());
		meta = state.getBlock().getMetaFromState(state);
	}

	public static ItemInfo readFromNBT(final NBTTagCompound tag) {
		final Item item = ForgeRegistries.ITEMS.getValue(new ResourceLocation(tag.getString("item")));
		final int meta = tag.getInteger("meta");
		if (tag.hasKey("nbt"))
			return item == null ? EMPTY : new ItemInfo(item, meta, tag.getCompoundTag("nbt"));
		return item == null ? EMPTY : new ItemInfo(item, meta);
	}


	private void checkWildcard() {
		// This checks if the item has sub items or not.
		// If not, accept any item that matches this, otherwise
		// Only accept items with meta 0
		final NonNullList<ItemStack> subItems = NonNullList.create();
        item.getSubItems(item.getCreativeTab() == null ? CreativeTabs.SEARCH : item.getCreativeTab(), subItems);
		if (subItems.size() <= 1)
			isWildcard = true;
	}

	//StackInfo

	@Override
	public String toString() {
		return ForgeRegistries.ITEMS.getKey(item) + (meta == 0 ? "" : ":" + meta);
	}

	@Nonnull
	@Override
	public ItemStack getItemStack() {
		if (item == Items.AIR)
			return ItemStack.EMPTY;
		final ItemStack stack = new ItemStack(item, 1, meta);
		if (nbt != null)
			stack.setTagCompound(nbt);

		return stack;
	}

	@Override
	public boolean hasBlock() {
		return item instanceof ItemBlock;
	}

	@Nonnull
	@Override
	public Block getBlock() {
		return Block.getBlockFromItem(item);
	}

	@Nonnull
	@Override
	public IBlockState getBlockState() {
		if (item == Items.AIR)
			return Blocks.AIR.getDefaultState();
		try {
			//noinspection deprecation
			return Block.getBlockFromItem(item).getStateFromMeta(meta);
		} catch (final Exception e) {
			return Block.getBlockFromItem(item).getDefaultState();
		}
	}

	@Override
	public int getMeta() {
		return isWildcard ? -1 : meta;
	}

	@Override
	public NBTTagCompound writeToNBT(final NBTTagCompound tag) {
		final ResourceLocation key = ForgeRegistries.ITEMS.getKey(item);
		tag.setString("item", key == null ? "" : key.toString());
		tag.setInteger("meta", meta);
		if (nbt != null && !nbt.hasNoTags())
			tag.setTag("nbt", nbt);
		return tag;
	}

	@Override
	public boolean isValid() {
		return item != Items.AIR && meta <= OreDictionary.WILDCARD_VALUE;
	}

	@Override
	public int hashCode() {
		return item.hashCode();
	}

	@Override
	public boolean equals(final Object obj) {
		if (isWildcard) {
			if (obj instanceof ItemInfo)
				return ItemStack.areItemsEqualIgnoreDurability(((ItemInfo) obj).getItemStack(), getItemStack());
			else if (obj instanceof ItemStack)
				return ItemStack.areItemsEqualIgnoreDurability((ItemStack) obj, getItemStack());
			else if (obj instanceof BlockInfo)
				return ItemStack.areItemsEqualIgnoreDurability(((BlockInfo) obj).getItemStack(), getItemStack());
			else if (obj instanceof Block) {
				final BlockInfo block = new BlockInfo((Block) obj);
				return ItemStack.areItemsEqualIgnoreDurability(block.getItemStack(), getItemStack());
			} else if (obj instanceof Item) {
				final ItemInfo item = new ItemInfo((Item) obj);
				return ItemStack.areItemsEqualIgnoreDurability(item.getItemStack(), getItemStack());
			}
		} else if (obj instanceof ItemInfo)
			return ItemStack.areItemStacksEqual(((ItemInfo) obj).getItemStack(), getItemStack());
		else if (obj instanceof ItemStack)
			return ItemStack.areItemStacksEqual((ItemStack) obj, getItemStack());
		else if (obj instanceof BlockInfo)
			return ItemStack.areItemStacksEqual(((BlockInfo) obj).getItemStack(), getItemStack());
		else if (obj instanceof Block) {
			final BlockInfo block = new BlockInfo((Block) obj);
			return ItemStack.areItemStacksEqual(block.getItemStack(), getItemStack());
		} else if (obj instanceof Item) {
			final ItemInfo item = new ItemInfo((Item) obj);
			return ItemStack.areItemStacksEqual(item.getItemStack(), getItemStack());
		}
		return false;
	}

	@Nonnull
	public Item getItem() {
		return item;
	}

	@Nullable
	public NBTTagCompound getNbt() {
		return nbt;
	}

	public ItemInfo copy()
	{
		final ItemInfo info = new ItemInfo(item, meta, nbt);
		info.isWildcard = isWildcard;
		return info;
	}
}