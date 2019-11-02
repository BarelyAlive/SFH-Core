package mod.sfhcore.capabilities;

import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.items.CustomBucket;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;

public class CapabilityCustomBucketFluidHandler {
	@CapabilityInject(IFluidHandler.class)
	public static Capability<IFluidHandler> FLUID_HANDLER_CAPABILITY = null;
	@CapabilityInject(IFluidHandlerItem.class)
	public static Capability<IFluidHandlerItem> FLUID_HANDLER_ITEM_CAPABILITY = null;
	@CapabilityInject(String.class)
	public static Capability<String> BUCKET_MATERIAL_CAPABILITY = null;
	@CapabilityInject(Integer.class)
	public static Capability<Integer> BUCKET_COLOR_CAPABILITY = null;

	public static void register()
	{
		CapabilityManager.INSTANCE.register(IFluidHandler.class, new DefaultFluidHandlerStorage<>(), () -> new FluidTank(Fluid.BUCKET_VOLUME));

		CapabilityManager.INSTANCE.register(IFluidHandlerItem.class, new DefaultFluidHandlerStorage<>(), () -> new FluidHandlerItemStack(new ItemStack(new CustomBucket(null, null, null, null, 0, null)), Fluid.BUCKET_VOLUME));
	}

	private static class DefaultFluidHandlerStorage<T extends IFluidHandler> implements Capability.IStorage<T> {
		@Override
		public NBTBase writeNBT(final Capability<T> capability, final T instance, final EnumFacing side)
		{
			System.out.println("writeNBT");
			System.out.println(instance);
			if (instance instanceof CustomBucket)
			{
				final NBTTagCompound nbt = new NBTTagCompound();
				final CustomBucket tank = (CustomBucket) instance;
				final FluidStack fluid = tank.drain(Integer.MAX_VALUE, false);
				if (fluid != null)
					nbt.setString("FluidName", FluidRegistry.getFluidName(fluid));
				else
					nbt.setString("Empty", "");
				nbt.setString("Material", tank.getMaterial());
				return nbt;
			}
			return new NBTTagCompound();
		}

		@Override
		public void readNBT(final Capability<T> capability, T instance, final EnumFacing side, final NBTBase nbt)
		{
			System.out.println("readNBT");
			System.out.println(instance);
			if (instance instanceof CustomBucket)
			{
				final NBTTagCompound tags = (NBTTagCompound) nbt;
				final CustomBucket bucket = (CustomBucket) instance;
				if (tags.hasKey("Empty"))
					instance = (T) BucketHandler.getBucketFromFluid(null, tags.getString("Material"));
				else
					instance = (T) BucketHandler.getBucketFromFluid(FluidRegistry.getFluid(tags.getString("FluidName")), tags.getString("Material"));
			}
		}
	}
}
