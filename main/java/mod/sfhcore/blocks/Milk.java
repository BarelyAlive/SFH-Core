package mod.sfhcore.blocks;

import mod.sfhcore.Constants;
import mod.sfhcore.handler.ModFluids;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.BlockRenderLayer;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;

public class Milk extends BlockFluidClassic{

	public Milk() {
		super(ModFluids.FLUID_MILK, Material.WATER);
		setRegistryName(Constants.MOD_ID, Constants.MILK);
		setUnlocalizedName(Constants.MILK);
		setLightOpacity(255);
		setTemperature(FluidRegistry.WATER.getTemperature());
		setDensity(FluidRegistry.WATER.getDensity());
	}
	
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.SOLID;
	}

	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		return getBlockState().getBaseState().withProperty(LEVEL, meta);
	}

	@Override
	public Fluid getFluid() {
		return ModFluids.FLUID_MILK;
	}
}
