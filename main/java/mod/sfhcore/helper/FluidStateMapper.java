package mod.sfhcore.helper;

import javax.annotation.Nonnull;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class FluidStateMapper extends StateMapperBase implements ItemMeshDefinition {

    private final ModelResourceLocation location;

	public FluidStateMapper(final String modid, final Fluid fluid) {

        location = new ModelResourceLocation(new ResourceLocation(modid, fluid.getName()), fluid.getName());
	}

	@Nonnull
	@Override
	protected ModelResourceLocation getModelResourceLocation(@Nonnull final IBlockState state) {
		return location;
	}

	@Nonnull
	@Override
	public ModelResourceLocation getModelLocation(@Nonnull final ItemStack stack) {
		return location;
	}
}
