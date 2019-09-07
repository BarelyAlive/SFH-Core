package mod.sfhcore.items;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class CustomBucketMilk extends Item
{
	private ItemStack empty;

	public CustomBucketMilk(final CreativeTabs tab, final ItemStack empty, final ResourceLocation loc)
	{
		setMaxStackSize(1);
		setCreativeTab(tab);
		setRegistryName(loc);
		this.empty = empty != null ? empty : ItemStack.EMPTY;
	}

	/**
	 * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
	 * the Item before the action is complete.
	 */
	@Override
	public ItemStack onItemUseFinish(final ItemStack stack, final World worldIn, final EntityLivingBase entityLiving)
	{
		if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
		if (entityLiving instanceof EntityPlayerMP)
		{
			EntityPlayerMP entityplayermp = (EntityPlayerMP)entityLiving;
			CriteriaTriggers.CONSUME_ITEM.trigger(entityplayermp, stack);
			entityplayermp.addStat(StatList.getObjectUseStats(this));
		}

		if (entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
			stack.shrink(1);

		return stack.isEmpty() ? empty : stack;
	}

	/**
	 * How long it takes to use or consume an item
	 */
	@Override
	public int getMaxItemUseDuration(final ItemStack stack)
	{
		return 32;
	}

	/**
	 * returns the action that specifies what animation to play when the items is being used
	 */
	@Override
	public EnumAction getItemUseAction(final ItemStack stack)
	{
		return EnumAction.DRINK;
	}

	@Override
	public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(final ItemStack stack, final net.minecraft.nbt.NBTTagCompound nbt) {
		return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn)
	{
		playerIn.setActiveHand(handIn);
		return new ActionResult<>(EnumActionResult.SUCCESS, playerIn.getHeldItem(handIn));
	}
}