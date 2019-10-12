package mod.sfhcore.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CubeTransparent extends Cube{

	public CubeTransparent(final Material material, final float resistance, final float hardness, final ResourceLocation loc) {
		super(material, resistance, hardness, loc);
		setLightOpacity(15);
	}

	public CubeTransparent(final Material material, final float resistance, final float hardness, final ResourceLocation loc, final TileEntity te) {
		super(material, resistance, hardness, loc, te);
		setLightOpacity(15);
	}

	@Override
	public int quantityDropped(final Random random)
	{
		return 1;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return BlockRenderLayer.CUTOUT;
	}

	@Override
	public boolean isFullCube(final IBlockState state)
	{
		return false;
	}

	@Override
	protected boolean canSilkHarvest()
	{
		return true;
	}
}
