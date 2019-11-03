package mod.sfhcore.items;

import javax.annotation.Nullable;

import mod.sfhcore.capabilities.CustomBucketCapability;
import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.helper.PlayerInventory;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.translation.I18n;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class CustomBucket extends Item implements IFluidHandler{

	/** field for checking if the bucket has been filled. */
	private Block containedBlock;
	private Fluid fluid;
	private int color;
	private String material;
	private String localizedName;

	private ItemStack empty;

	public CustomBucket(final Block b, final ResourceLocation loc, final ItemStack empty, final CreativeTabs tab, final int color, final String material)
	{
		maxStackSize = 1;
		containedBlock = b;
		this.empty = empty;
		this.color = color;
		this.material = material;
		fluid = FluidRegistry.lookupFluidForBlock(b);
		localizedName = "";
		this.setRegistryName(loc);
		setUnlocalizedName(loc.getResourcePath());
		setCreativeTab(tab);
		setContainerItem(empty.getItem());
	}

	public int getColor()
	{
		return color;
	}

	public Fluid getFluid()
	{
		return fluid;
	}

	public String getMaterial()
	{
		return material;
	}

	private FluidStack getFluidContained()
	{
		if(!isAir()) {
			final Fluid f = FluidRegistry.lookupFluidForBlock(containedBlock);
			return new FluidStack(f, 1000);
		}
		return null;
	}

	public void setColor(final int color)
	{
		this.color = color;
	}

	public void setFluid(final Fluid f)
	{
		fluid = f;
	}

	public void setMaterial(final String material)
	{
		this.material = material;
	}

	public void setLocalizedName(final String localizedName) {
		this.localizedName = localizedName;
	}

	public String getLocalizedName() {
		return localizedName;
	}

	@Override
	public String getItemStackDisplayName(final ItemStack stack) {
		if (stack.isEmpty())
			return "";
		else if (!(stack.getItem() instanceof CustomBucket))
			return I18n.translateToLocal(getUnlocalizedNameInefficiently(stack) + ".name").trim();
		else
			return ((CustomBucket) stack.getItem()).getLocalizedName();
	}

	@Override
	public int getItemBurnTime(final ItemStack itemStack)
	{
		final Fluid f = FluidRegistry.lookupFluidForBlock(containedBlock);
		if(f != null)
			if(f.getTemperature() >= FluidRegistry.LAVA.getTemperature())
				return (int)(20000 * ((float)f.getTemperature() / (float)FluidRegistry.LAVA.getTemperature()));
		return 0;
	}

	private Block getContainedB(final ItemStack stack)
	{
		return ((CustomBucket) stack.getItem()).containedBlock;
	}

	public Block getContainedBlock()
	{
		return containedBlock;
	}

	private boolean isAir()
	{
		return containedBlock == Blocks.AIR;
	}
	
	private boolean isMilk()
	{
		return !isAir() && getContainedBlock() != null &&
    		FluidRegistry.lookupFluidForBlock(getContainedBlock()) == FluidRegistry.getFluid("milk");
	}

	/**
	 * Called when the equipped item is right clicked.
	 */
	@Override
	public ActionResult<ItemStack> onItemRightClick(final World worldIn, final EntityPlayer playerIn, final EnumHand handIn)
	{
		final boolean flag = isAir();
		final ItemStack held = playerIn.getHeldItem(handIn);
		final RayTraceResult raytraceresult = rayTrace(worldIn, playerIn, flag);

		if (raytraceresult == null)	
			if (isMilk())
			{
				playerIn.setActiveHand(handIn);
				return new ActionResult<>(EnumActionResult.SUCCESS, held);
			}
			else
				return new ActionResult<>(EnumActionResult.PASS, held);

		if(raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
		{
			final BlockPos pos = raytraceresult.getBlockPos();
			if (!flag && containedBlock != null)
			{				
				if (!playerIn.canPlayerEdit(pos.offset(raytraceresult.sideHit), raytraceresult.sideHit, held))
					return new ActionResult<>(EnumActionResult.FAIL, held);
				else
				{
					final boolean flag1 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
					final BlockPos pos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? pos : pos.offset(raytraceresult.sideHit);

					if (!playerIn.canPlayerEdit(pos1, raytraceresult.sideHit, held))
						return new ActionResult<>(EnumActionResult.FAIL, held);
					else
					{
						final Block block = worldIn.getBlockState(pos1).getBlock();
						Fluid fluid_from_block = FluidRegistry.lookupFluidForBlock(block);

						fluid_from_block = FluidRegistry.lookupFluidForBlock(containedBlock);
						if (block.isReplaceable(worldIn, pos1) && fluid_from_block != null)
						{
							if(this.isMilk() && playerIn.isSneaking())
							{
								playerIn.setActiveHand(handIn);
								return new ActionResult<>(EnumActionResult.SUCCESS, held);
							}
							else if(fluid_from_block.doesVaporize(new FluidStack(fluid_from_block, 1000)) && worldIn.provider.doesWaterVaporize())
								fluid_from_block.vaporize(playerIn, worldIn, pos1, getFluidContained());
							else if (containedBlock.equals(Blocks.WATER))
								worldIn.setBlockState(pos1, Blocks.FLOWING_WATER.getDefaultState());
							else if (containedBlock.equals(Blocks.LAVA))
								worldIn.setBlockState(pos1, Blocks.FLOWING_LAVA.getDefaultState());
							else if(!playerIn.isSneaking())
								worldIn.setBlockState(pos1, containedBlock.getDefaultState());
								
							return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, held.getItem().getContainerItem(held)) : new ActionResult(EnumActionResult.SUCCESS, held);
						}
					}
				}
			}
			else if (!playerIn.canPlayerEdit(pos, raytraceresult.sideHit, held))
				return new ActionResult<>(EnumActionResult.FAIL, held);
			else
			{
				final boolean flag1 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);

				final Block block = worldIn.getBlockState(pos).getBlock();
				final Fluid fluid_from_block = FluidRegistry.lookupFluidForBlock(block);
				if (fluid_from_block != null)
					if (BucketHandler.getMaxTemperaturFromBucket(((CustomBucket)held.getItem()).getMaterial()) == -1 || BucketHandler.getMaxTemperaturFromBucket(((CustomBucket)held.getItem()).getMaterial()) == 0 || BucketHandler.getMaxTemperaturFromBucket(((CustomBucket)held.getItem()).getMaterial()) >= fluid_from_block.getTemperature())
					{
						worldIn.setBlockToAir(pos);
						final CustomBucket new_bucket = BucketHandler.getBucketFromFluid(fluid_from_block, ((CustomBucket)held.getItem()).getMaterial());
						playerIn.inventory.markDirty();
	
						if(!playerIn.capabilities.isCreativeMode)
						{
							PlayerInventory.tryAddItem(playerIn, new ItemStack(new_bucket));
							held.shrink(1);
						}
						return new ActionResult(EnumActionResult.SUCCESS, held);
					}
			}
		}

		return new ActionResult<>(EnumActionResult.FAIL, held);
	}

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(final ItemStack stack, @Nullable final net.minecraft.nbt.NBTTagCompound nbt) {
        if (this.getClass() == CustomBucket.class)
			return new CustomBucketCapability(stack);
		else
			return super.initCapabilities(stack, nbt);
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    @Override
	public ItemStack onItemUseFinish(ItemStack stack, final World worldIn, final EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote)
        	if(isMilk())
        	{
        		entityLiving.curePotionEffects(new ItemStack(Items.MILK_BUCKET)); // FORGE - move up so stack.shrink does not turn stack into air
        		
        		if (!((EntityPlayer)entityLiving).capabilities.isCreativeMode) {
					ItemStack copy = stack.copy();
					stack.shrink(1);
					((EntityPlayer) entityLiving).addItemStackToInventory(this.getContainerItem(copy));
				}
        	}
       
        if(isAir() && getContainedBlock() != null)
	        if (FluidRegistry.lookupFluidForBlock(((CustomBucket)stack.getItem()).getContainedBlock()) == FluidRegistry.getFluid("milk"))
	        {
		        if (entityLiving instanceof EntityPlayerMP)
		        {
		            final EntityPlayerMP entityplayermp = (EntityPlayerMP)entityLiving;
		            CriteriaTriggers.CONSUME_ITEM.trigger(entityplayermp, stack);
		            entityplayermp.addStat(StatList.getObjectUseStats(this));
		        }
		
		        if (entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
		        {
		        	final CustomBucket emptyBucket = BucketHandler.getBucketFromFluid(null, ((CustomBucket)stack.getItem()).getMaterial());
		            ((EntityPlayer)entityLiving).getHeldItem(((EntityPlayer)entityLiving).getActiveHand()).shrink(1);
		        }
	        }

        return stack.isEmpty() ? empty : stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
	public int getMaxItemUseDuration(final ItemStack stack)
    {
    	if(isMilk())
	    	return 32;
    	return 0;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
	public EnumAction getItemUseAction(final ItemStack stack)
    {
    	if(isMilk())
    			return EnumAction.DRINK;
        return EnumAction.NONE;
    }

	@Override
	public IFluidTankProperties[] getTankProperties() {
		return null;
	}

	@Override
	public int fill(final FluidStack resource, final boolean doFill)
	{
		if (isAir())
			return 1000;
		return 0;
	}

	@Override
	public FluidStack drain(final FluidStack resource, final boolean doDrain)
	{
		if(fluid == resource.getFluid())
			return new FluidStack(resource.getFluid(), 1000);
		return null;
	}

	@Override
	public FluidStack drain(final int maxDrain, final boolean doDrain)
	{
		if (maxDrain >= 1000)
			return new FluidStack(fluid, 1000);
		return null;
	}
}