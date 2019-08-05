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
	
	public CustomBucket(Block b, ResourceLocation loc, ItemStack empty, CreativeTabs tab, int color, String material)
	{
		this.maxStackSize = 1;
        this.containedBlock = b;
        this.empty = empty;
        this.color = color;
        this.material = material;
        this.fluid = FluidRegistry.lookupFluidForBlock(b);
        this.localizedName = "";
        this.setUnlocalizedName(loc.getResourcePath());
        this.setRegistryName(loc);
        this.setCreativeTab(tab);
        this.setContainerItem(empty.getItem());
        //this.setHasSubtypes(true);
	}
	
	public int getColor()
	{
		return this.color;
	}
	
	public Fluid getFluid()
	{
		return this.fluid;
	}
	
	public String getMaterial()
	{
		return this.material;
	}
	
	private FluidStack getFluidContained()
	{
		if(!isAir()) {
			Fluid f = FluidRegistry.lookupFluidForBlock(this.containedBlock);
			return new FluidStack(f, 1000);
		}
		return null;
	}
	
	public void setColor(int color)
	{
		this.color = color;
	}
	
	public void setFluid(Fluid f)
	{
		this.fluid = f;
	}
	
	public void setMaterial(String material)
	{
		this.material = material;
	}
	
	public void setLocalizedName(String localizedName) {
		this.localizedName = localizedName;
	}
	
	public String getLocalizedName() {
		return this.localizedName;
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack stack) {
		if (stack.isEmpty())
		{
			return "";
		}
		else if (!(stack.getItem() instanceof CustomBucket))
		{
			return I18n.translateToLocal(this.getUnlocalizedNameInefficiently(stack) + ".name").trim();
		}
		else
		{
			return ((CustomBucket) stack.getItem()).getLocalizedName();
		}
	}
		
	@Override
	public int getItemBurnTime(ItemStack itemStack)
	{
		Fluid f = FluidRegistry.lookupFluidForBlock(this.containedBlock);
		if(f != null)
		{
			if(f.getTemperature() >= FluidRegistry.LAVA.getTemperature())
			{
				return 18000 * (f.getTemperature() / FluidRegistry.LAVA.getTemperature());
			}
		}
		return 0;
	}
	
	private Block getContainedB(ItemStack stack)
	{
		return ((CustomBucket) stack.getItem()).containedBlock;
	}
	
	public Block getContainedBlock()
	{
		return this.containedBlock;
	}
	
	private boolean isAir()
	{
		return this.containedBlock == Blocks.AIR;
	}

    /**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		/*
		if(!worldIn.isRemote)
		{
	        return new ActionResult<ItemStack>(EnumActionResult.FAIL, playerIn.getHeldItem(handIn));
		}
		*/
		boolean flag = isAir();
        ItemStack held = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        
        if (FluidRegistry.lookupFluidForBlock(((CustomBucket) held.getItem()).getContainedBlock()) == FluidRegistry.getFluid("milk"))
        {
            playerIn.setActiveHand(handIn);
            return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, held);
        }
        
        if (raytraceresult == null)
            return new ActionResult<ItemStack>(EnumActionResult.PASS, held);
        
        if(raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK)
        {
        	BlockPos pos = raytraceresult.getBlockPos();
        	if (!flag && this.containedBlock != null)
        	{
        		if (!playerIn.canPlayerEdit(pos.offset(raytraceresult.sideHit), raytraceresult.sideHit, held))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
                }
        		else
        		{
        			boolean flag1 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    				BlockPos pos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? pos : pos.offset(raytraceresult.sideHit);
    				
    				if (!playerIn.canPlayerEdit(pos1, raytraceresult.sideHit, held))
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
                    }
        			else
        			{
        				
        				Block block = worldIn.getBlockState(pos1).getBlock();
        				Fluid fluid_from_block = FluidRegistry.lookupFluidForBlock(block);
        				
        				fluid_from_block = FluidRegistry.lookupFluidForBlock(this.containedBlock);
						if (block.isReplaceable(worldIn, pos1) && fluid_from_block != null)
						{
							if(fluid_from_block.doesVaporize(new FluidStack(fluid_from_block, 1000)) && worldIn.provider.doesWaterVaporize())
							{
								fluid_from_block.vaporize(playerIn, worldIn, pos1, getFluidContained());
							}
							else
							{
								if (this.containedBlock.equals(Blocks.WATER))
								{
        							worldIn.setBlockState(pos1, Blocks.FLOWING_WATER.getDefaultState());
								}
								else if (this.containedBlock.equals(Blocks.LAVA))
								{
        							worldIn.setBlockState(pos1, Blocks.FLOWING_LAVA.getDefaultState());
								}
								else
								{
        							worldIn.setBlockState(pos1, this.containedBlock.getDefaultState());
								}
							}
							return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, held.getItem().getContainerItem(held)) : new ActionResult(EnumActionResult.SUCCESS, held);
						}
    				}
        		}
			}
			else
			{
        		if (!playerIn.canPlayerEdit(pos, raytraceresult.sideHit, held))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
                }
        		else
        		{
        			boolean flag1 = worldIn.getBlockState(pos).getBlock().isReplaceable(worldIn, pos);
    				//BlockPos pos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? pos : pos.offset(raytraceresult.sideHit);
    				
    				Block block = worldIn.getBlockState(pos).getBlock();
    				Fluid fluid_from_block = FluidRegistry.lookupFluidForBlock(block);
    				if (fluid_from_block != null)
    				{
    					worldIn.setBlockToAir(pos);
    					CustomBucket new_bucket = BucketHandler.getBucketFromFluid(fluid_from_block, ((CustomBucket)held.getItem()).getMaterial());
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
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.NBTTagCompound nbt) {
        if (this.getClass() == CustomBucket.class)
        {
            return new CustomBucketCapability(stack);
        }
        else
        {
            return super.initCapabilities(stack, nbt);
        }
    }

    /**
     * Called when the player finishes using this Item (E.g. finishes eating.). Not called when the player stops using
     * the Item before the action is complete.
     */
    @Override
	public ItemStack onItemUseFinish(ItemStack stack, World worldIn, EntityLivingBase entityLiving)
    {
        if (!worldIn.isRemote) entityLiving.curePotionEffects(stack); // FORGE - move up so stack.shrink does not turn stack into air
        
        if (FluidRegistry.lookupFluidForBlock(((CustomBucket)stack.getItem()).getContainedBlock()) == FluidRegistry.getFluid("milk"))
        {
	        if (entityLiving instanceof EntityPlayerMP)
	        {
	            EntityPlayerMP entityplayermp = (EntityPlayerMP)entityLiving;
	            CriteriaTriggers.CONSUME_ITEM.trigger(entityplayermp, stack);
	            entityplayermp.addStat(StatList.getObjectUseStats(this));
	        }
	
	        if (entityLiving instanceof EntityPlayer && !((EntityPlayer)entityLiving).capabilities.isCreativeMode)
	        {
	        	CustomBucket emptyBucket = BucketHandler.getBucketFromFluid(null, ((CustomBucket)stack.getItem()).getMaterial());
	            stack.shrink(1);
	            ((EntityPlayer)entityLiving).addItemStackToInventory(new ItemStack(emptyBucket));
	        }
        }

        return stack.isEmpty() ? empty : stack;
    }

    /**
     * How long it takes to use or consume an item
     */
    @Override
	public int getMaxItemUseDuration(ItemStack stack)
    {
    	if (FluidRegistry.lookupFluidForBlock(((CustomBucket)stack.getItem()).getContainedBlock()) == FluidRegistry.getFluid("milk"))
    		return 32;
    	return 0;
    }

    /**
     * returns the action that specifies what animation to play when the items is being used
     */
    @Override
	public EnumAction getItemUseAction(ItemStack stack)
    {
    	if (FluidRegistry.lookupFluidForBlock(((CustomBucket)stack.getItem()).getContainedBlock()) == FluidRegistry.getFluid("milk"))
    		return EnumAction.DRINK;
        return EnumAction.NONE;
    }

    @Override
	public IFluidTankProperties[] getTankProperties() {
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill)
	{
		if (isAir()) {
				return 1000;
		}
		return 0;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain)
	{
		if(this.fluid == resource.getFluid())
		{
			return new FluidStack(resource.getFluid(), 1000);
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain)
	{
		if (maxDrain >= 1000)
		{
			return new FluidStack(this.fluid, 1000);
		}
		return null;
	}
}