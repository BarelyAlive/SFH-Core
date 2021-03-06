package mod.sfhcore.helper;

import javax.annotation.Nonnull;

import mod.sfhcore.items.CustomBucket;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;

public class BucketHelper
{
	public static boolean isBucketWithFluidMaterial(@Nonnull final ItemStack held, final Material material)
	{
		final FluidStack f = FluidUtil.getFluidContained(held);
		if(f == null) return false;
		if(f.getFluid().getBlock() == null) return false;
		final IBlockState blockFluid = f.getFluid().getBlock().getDefaultState();
		final Material m = blockFluid.getMaterial();
		if(!m.equals(material)) return false;
		if(held.getItem().equals(Items.WATER_BUCKET)) return true;

		return held.getItem() instanceof UniversalBucket || held.getItem() instanceof CustomBucket;
	}
}
