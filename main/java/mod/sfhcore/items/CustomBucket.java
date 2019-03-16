package mod.sfhcore.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.helper.FluidHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.*;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.*;
import net.minecraftforge.fml.common.registry.GameRegistry;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;

class BucketItem {
	public Block block;
	public Fluid fluid;
	public ItemStack result;
}

public class CustomBucket extends Item implements IFluidHandler{
	
	/** field for checking if the bucket has been filled. */
    private Block containedBlock;
    
    /** List of accepted Fluids */
    private List<BucketItem> bucketList;

	private ItemStack empty;
	
	public CustomBucket(Block containedBlockIn, String unlocalizedName, ItemStack empty){
		this.bucketList = new ArrayList<BucketItem>();
		this.maxStackSize = 1;
        this.containedBlock = containedBlockIn;
        this.empty = empty;
        this.setUnlocalizedName(unlocalizedName);
	}
	
	public ItemStack getBucketForBlock(Block b) {
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(bl.block.equals(b))
			{
				return bl.result;
			}
		}
		
		return empty;
	}
	
	public ItemStack getBucketForFluid(Fluid f) {
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(bl.fluid.equals(f))
			{
				return bl.result;
			}
		}
		
		return empty;
	}

	public ItemStack getBucketForBlockAndFluid(Block b, Fluid f) {
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(!(bl.block.equals(b)))
			{
				continue;
			}
			if(!(bl.fluid.equals(f)))
			{
				continue;
			}
			return bl.result;
		}
		
		return empty;
	}
	
	public void addBucket(Block b, Fluid f, ItemStack result)
	{
		BucketItem bl = new BucketItem();
		bl.block = b;
		bl.fluid = f;
		bl.result = result;
		this.bucketList.add(bl);
	}
	
	public boolean hasAcceptedFluid(Fluid f)
	{
		if ((this.getBucketForFluid(f)).equals(this.empty))
		{
			return false;
		}
		return true;
	}
	
	public boolean hasAcceptedBlock(Block b)
	{
		if ((this.getBucketForBlock(b)).equals(this.empty))
		{
			return false;
		}
		return true;
	}
	
	public boolean hasAcceptedFluidAndBlock(Block b, Fluid f)
	{
		if ((this.getBucketForBlockAndFluid(b, f)).equals(this.empty))
		{
			return false;
		}
		return true;
	}
	
	public void removeAcceptedFluids(Fluid f)
	{
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(bl.fluid.equals(f))
			{
				this.bucketList.remove(i);
				i--;
			}
		}
	}
	
	public void removeAcceptedBlocks(Block b)
	{
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(bl.block.equals(b))
			{
				this.bucketList.remove(i);
				i--;
			}
		}
	}
	
	public void removeAcceptedBlocksAndFluids(Block b, Fluid f)
	{
		int bucketListSize = this.bucketList.size();
		BucketItem bl;
		
		for(int i = 0; i < bucketListSize; i++)
		{
			bl = this.bucketList.get(i);
			if(bl.block.equals(b) && bl.fluid.equals(f))
			{
				this.bucketList.remove(i);
				i--;
			}
		}
	}
	
	public List<BucketItem> getAcceptedFluids()
	{
		return this.bucketList;
	}
	
	@Override
	public int getItemBurnTime(ItemStack itemStack) {
		if(FluidHelper.getFluidForFilledItem(itemStack.getItem()).getFluid() == FluidRegistry.LAVA)
		return 20000;
		return 0;
	}

    /**
     * Called when the equipped item is right clicked.
     */
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
        boolean flag = this.containedBlock == Blocks.AIR;
        ItemStack itemstack = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        ActionResult<ItemStack> ret = net.minecraftforge.event.ForgeEventFactory.onBucketUse(playerIn, worldIn, itemstack, raytraceresult);
        if (ret != null) return ret;

        if (raytraceresult == null)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else if (raytraceresult.typeOfHit != RayTraceResult.Type.BLOCK)
        {
            return new ActionResult<ItemStack>(EnumActionResult.PASS, itemstack);
        }
        else
        {
            BlockPos blockpos = raytraceresult.getBlockPos();

            if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
            }
            else if (flag)
            {
                if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else
                {
                    IBlockState iblockstate = worldIn.getBlockState(blockpos);
                    Material material = iblockstate.getMaterial();
                    Block b2 = iblockstate.getBlock();

                	if(((Integer)iblockstate.getValue(BlockLiquid.LEVEL)).intValue() == 0)
                	{
                		if(this.hasAcceptedBlock(b2))
                		{
	                        worldIn.setBlockState(blockpos, Blocks.AIR.getDefaultState(), 11);
	                        playerIn.addStat(StatList.getObjectUseStats(this));
	                        if(b2 == Blocks.FLOWING_LAVA)
	                        	playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL_LAVA, 1.0F, 1.0F);
	                        	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, (this.getBucketForBlock(b2)).getItem()));
	                    	}
	                        else {
	                        	playerIn.playSound(SoundEvents.ITEM_BUCKET_FILL, 1.0F, 1.0F);
	                        	return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, this.fillBucket(itemstack, playerIn, (this.getBucketForBlock(b2)).getItem()));
	                    
	                        }
                		}
	                    else
	                    {
	                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
	                    }
                	}
                }
            else
            {
                boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
                BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);

                if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, itemstack))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
                else if (this.tryPlaceContainedLiquid(playerIn, worldIn, blockpos1))
                {
                    if (playerIn instanceof EntityPlayerMP)
                    {
                        CriteriaTriggers.PLACED_BLOCK.trigger((EntityPlayerMP)playerIn, blockpos1, itemstack);
                    }

                    playerIn.addStat(StatList.getObjectUseStats(this));
                    return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, empty) : new ActionResult(EnumActionResult.SUCCESS, itemstack);
                }
                else
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, itemstack);
                }
            }
        }
    }

    private ItemStack fillBucket(ItemStack emptyBuckets, EntityPlayer player, Item fullBucket)
    {
        if (player.capabilities.isCreativeMode)
        {
            return emptyBuckets;
        }
        else
        {
            emptyBuckets.shrink(1);

            if (emptyBuckets.isEmpty())
            {
                return new ItemStack(fullBucket);
            }
            else
            {
                if (!player.inventory.addItemStackToInventory(new ItemStack(fullBucket)))
                {
                    player.dropItem(new ItemStack(fullBucket), false);
                }

                return emptyBuckets;
            }
        }
    }

    public boolean tryPlaceContainedLiquid(@Nullable EntityPlayer player, World worldIn, BlockPos posIn)
    {
        if (this.containedBlock == Blocks.AIR)
        {
            return false;
        }
        else
        {
            IBlockState iblockstate = worldIn.getBlockState(posIn);
            Material material = iblockstate.getMaterial();
            boolean flag = !material.isSolid();
            boolean flag1 = iblockstate.getBlock().isReplaceable(worldIn, posIn);

            if (!worldIn.isAirBlock(posIn) && !flag && !flag1)
            {
                return false;
            }
            else
            {
                if (worldIn.provider.doesWaterVaporize() && this.containedBlock == Blocks.FLOWING_WATER)
                {
                    int l = posIn.getX();
                    int i = posIn.getY();
                    int j = posIn.getZ();
                    worldIn.playSound(player, posIn, SoundEvents.BLOCK_FIRE_EXTINGUISH, SoundCategory.BLOCKS, 0.5F, 2.6F + (worldIn.rand.nextFloat() - worldIn.rand.nextFloat()) * 0.8F);

                    for (int k = 0; k < 8; ++k)
                    {
                        worldIn.spawnParticle(EnumParticleTypes.SMOKE_LARGE, (double)l + Math.random(), (double)i + Math.random(), (double)j + Math.random(), 0.0D, 0.0D, 0.0D);
                    }
                }
                
                if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);

                SoundEvent soundevent = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
                worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
                }
                
                if (!worldIn.isRemote && (flag || flag1) && !material.isLiquid())
                {
                    worldIn.destroyBlock(posIn, true);

                SoundEvent soundevent = this.containedBlock == Blocks.FLOWING_LAVA ? SoundEvents.ITEM_BUCKET_EMPTY_LAVA : SoundEvents.ITEM_BUCKET_EMPTY;
                worldIn.playSound(player, posIn, soundevent, SoundCategory.BLOCKS, 1.0F, 1.0F);
                worldIn.setBlockState(posIn, this.containedBlock.getDefaultState(), 11);
                
                }

                return true;
            }
        }
    }

    @Override
    public net.minecraftforge.common.capabilities.ICapabilityProvider initCapabilities(ItemStack stack, @Nullable net.minecraft.nbt.NBTTagCompound nbt) {
        if (this.getClass() == CustomBucket.class)
        {
            return new net.minecraftforge.fluids.capability.wrappers.FluidBucketWrapper(stack);
        }
        else
        {
            return super.initCapabilities(stack, nbt);
        }
    }

    @Override
	public IFluidTankProperties[] getTankProperties() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int fill(FluidStack resource, boolean doFill) {
		return 1000;
	}

	@Override
	public FluidStack drain(FluidStack resource, boolean doDrain) {
		if(this.hasAcceptedBlock(this.containedBlock))
		{
			return new FluidStack(FluidRegistry.lookupFluidForBlock(this.containedBlock), 1000);
		}
		return null;
	}

	@Override
	public FluidStack drain(int maxDrain, boolean doDrain) {
		if(this.hasAcceptedBlock(this.containedBlock))
		{
				return new FluidStack(FluidRegistry.lookupFluidForBlock(this.containedBlock), 1000);
		}
		return null;
	}
}