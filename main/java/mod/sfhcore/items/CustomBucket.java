package mod.sfhcore.items;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

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
    private Fluid fluid;
    
    /** List of accepted Fluids */
    private List<BucketItem> bucketList;

	private final ItemStack empty;
	
	public CustomBucket(Block b, ResourceLocation loc, ItemStack empty, CreativeTabs tab)
	{
		this.bucketList = new ArrayList<BucketItem>();
		this.maxStackSize = 1;
        this.containedBlock = b;
        this.empty = empty;
        this.fluid = FluidRegistry.lookupFluidForBlock(b);
        this.setRegistryName(loc);
        this.setCreativeTab(tab);
        this.setContainerItem(empty.getItem());
        this.addBucket(b, FluidRegistry.lookupFluidForBlock(b), new ItemStack(this));
	}
	
	private FluidStack getFluidContained()
	{
		if(!isAir()) {
			Fluid f = FluidRegistry.lookupFluidForBlock(this.containedBlock);
			return new FluidStack(f, 1000);
		}
		return null;
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
			if(f == null){
				if (bl.fluid.equals(f)) {
					return bl.result;
				} 
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
		if(f == null)
			return false;
		if ((this.getBucketForFluid(f)).equals(this.empty))
		{
			return false;
		}
		return true;
	}
	
	public boolean hasAcceptedBlock(Block b)
	{
		if( b == null || this.empty == null)
			return false;
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
	
	private boolean isAir()
	{
		return this.containedBlock == Blocks.AIR ? true : false;
	}

    /**
     * Called when the equipped item is right clicked.
     */
	@Override
    public ActionResult<ItemStack> onItemRightClick(World worldIn, EntityPlayer playerIn, EnumHand handIn)
    {
		boolean flag = isAir();
        ItemStack held = playerIn.getHeldItem(handIn);
        RayTraceResult raytraceresult = this.rayTrace(worldIn, playerIn, flag);
        
        if (raytraceresult == null)
            return new ActionResult<ItemStack>(EnumActionResult.PASS, held);
        
        if(raytraceresult.typeOfHit == RayTraceResult.Type.BLOCK && this.containedBlock != null)
        {
        	BlockPos blockpos = raytraceresult.getBlockPos();
        	
        	if (!worldIn.isBlockModifiable(playerIn, blockpos))
            {
                return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
            }
        	
        	else if (flag)
        	{
        		if (!playerIn.canPlayerEdit(blockpos.offset(raytraceresult.sideHit), raytraceresult.sideHit, held))
                {
                    return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
                }
        		else
        		{
        			boolean flag1 = worldIn.getBlockState(blockpos).getBlock().isReplaceable(worldIn, blockpos);
    				BlockPos blockpos1 = flag1 && raytraceresult.sideHit == EnumFacing.UP ? blockpos : blockpos.offset(raytraceresult.sideHit);
    				
    				if (!playerIn.canPlayerEdit(blockpos1, raytraceresult.sideHit, held))
                    {
                        return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
                    }
    				
        			else {
        				Block block = worldIn.getBlockState(blockpos1).getBlock();
        				if(!this.hasAcceptedBlock(block))
        					return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
        				
    					if (FluidUtil.tryPickUpFluid(held.getItem().getContainerItem(held), playerIn, worldIn, blockpos, raytraceresult.sideHit).success) {
    						
    						return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, getBucketForBlock(block)) : new ActionResult(EnumActionResult.SUCCESS, held);
    					}
    				} 
        		}
				
			}
			
			if (FluidUtil.tryPlaceFluid(playerIn, worldIn, blockpos, held,
					new FluidStack(FluidRegistry.lookupFluidForBlock(containedBlock), 1000)).success) {
				
				return !playerIn.capabilities.isCreativeMode ? new ActionResult(EnumActionResult.SUCCESS, new ItemStack(held.getItem().getContainerItem())) : new ActionResult(EnumActionResult.SUCCESS, held);
			}
        }
        
        return new ActionResult<ItemStack>(EnumActionResult.FAIL, held);
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