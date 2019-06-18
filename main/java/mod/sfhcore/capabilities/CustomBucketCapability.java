package mod.sfhcore.capabilities;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.items.CustomBucket;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.FluidTankProperties;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.IFluidTankProperties;

public class CustomBucketCapability implements IFluidHandlerItem, ICapabilityProvider {
    @Nonnull
    protected ItemStack container;

    public CustomBucketCapability(@Nonnull ItemStack container)
    {
        this.container = container;
    }

    @Nonnull
    @Override
    public ItemStack getContainer()
    {
        return container;
    }

    public boolean canFillFluidType(FluidStack fluidStack)
    {
        Fluid fluid = fluidStack.getFluid();
        return BucketHandler.getBucketFromFluid(fluid, ((CustomBucket)this.container.getItem()).getMaterial()) != null;
    }

    @Nullable
    public FluidStack getFluid()
    {
		if (this.container.getItem() == null)
		{
			return null;
		}
		if (((CustomBucket)this.container.getItem()).getContainedBlock() == null)
		{
			return null;
		}
        if (!(((CustomBucket)this.container.getItem()).getContainedBlock().equals(Blocks.AIR)))
        {
        	if (FluidRegistry.lookupFluidForBlock(((CustomBucket)this.container.getItem()).getContainedBlock()) != null)
        	{
        		return new FluidStack(FluidRegistry.lookupFluidForBlock(((CustomBucket)this.container.getItem()).getContainedBlock()), Fluid.BUCKET_VOLUME);
        	}
            return null;
        }
        else
        {
            return null;
        }
    }

    /**
     * @deprecated use the NBT-sensitive version {@link #setFluid(FluidStack)}
     */
    @Deprecated
    protected void setFluid(@Nullable Fluid fluid) {
        setFluid(new FluidStack(fluid, Fluid.BUCKET_VOLUME));
    }

    protected void setFluid(@Nullable FluidStack fluidStack)
    {
        if (fluidStack == null)
            container = new ItemStack(BucketHandler.getBucketFromFluid(null, ((CustomBucket)this.container.getItem()).getMaterial()));
        else
            container = new ItemStack(BucketHandler.getBucketFromFluid(fluidStack.getFluid(), ((CustomBucket)this.container.getItem()).getMaterial()));
    }

    @Override
    public IFluidTankProperties[] getTankProperties()
    {
        return new FluidTankProperties[] { new FluidTankProperties(getFluid(), Fluid.BUCKET_VOLUME) };
    }

    @Override
    public int fill(FluidStack resource, boolean doFill)
    {
        if (container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME || getFluid() != null || !canFillFluidType(resource))
        {
            return 0;
        }

        if (doFill)
        {
            setFluid(resource);
        }

        return Fluid.BUCKET_VOLUME;
    }

    @Nullable
    @Override
    public FluidStack drain(FluidStack resource, boolean doDrain)
    {
        if (container.getCount() != 1 || resource == null || resource.amount < Fluid.BUCKET_VOLUME)
        {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (fluidStack != null && fluidStack.isFluidEqual(resource))
        {
            if (doDrain)
            {
                setFluid((FluidStack) null);
            }
            return fluidStack;
        }

        return null;
    }

    @Nullable
    @Override
    public FluidStack drain(int maxDrain, boolean doDrain)
    {
        if (container.getCount() != 1 || maxDrain < Fluid.BUCKET_VOLUME)
        {
            return null;
        }

        FluidStack fluidStack = getFluid();
        if (fluidStack != null)
        {
            if (doDrain)
            {
                setFluid((FluidStack) null);
            }
            return fluidStack;
        }

        return null;
    }

    @Override
    public boolean hasCapability(@Nonnull Capability<?> capability, @Nullable EnumFacing facing)
    {
        return capability == CapabilityCustomBucketFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY;
    }

    @Override
    @Nullable
    public <T> T getCapability(@Nonnull Capability<T> capability, @Nullable EnumFacing facing)
    {
        if (capability == CapabilityCustomBucketFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY)
        {
            return CapabilityCustomBucketFluidHandler.FLUID_HANDLER_ITEM_CAPABILITY.cast(this);
        }
        return null;
    }
}
