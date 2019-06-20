package mod.sfhcore.helper;

import javax.annotation.Nonnull;

import mod.sfhcore.items.CustomBucket;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.UniversalBucket;

public class BucketHelper {

	public static boolean isBucketWithFluidMaterial(@Nonnull ItemStack held, Material material)
	{
		FluidStack f = FluidUtil.getFluidContained(held);
		if(f == null) return false;
		Block blockFluid = f.getFluid().getBlock();
		if(blockFluid == null) return false;
		Material m = blockFluid.getMaterial(blockFluid.getDefaultState());
		if(!m.equals(material)) return false;
		if(held.getItem().equals(Items.WATER_BUCKET)) return true;
		
		return held.getItem() instanceof UniversalBucket || held.getItem() instanceof CustomBucket;
	}
}
