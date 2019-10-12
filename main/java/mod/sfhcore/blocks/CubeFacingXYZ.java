package mod.sfhcore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CubeFacingXYZ  extends Block{

	public static final PropertyEnum<CubeFacingXYZ.EnumAxis> CUBE_AXIS = PropertyEnum.create("axis", CubeFacingXYZ.EnumAxis.class);

	public CubeFacingXYZ(final Material materialIn, final ResourceLocation loc) {
		super(materialIn);
		setDefaultState(blockState.getBaseState().withProperty(CUBE_AXIS, CubeFacingXYZ.EnumAxis.Y));
		setRegistryName(loc);
	}

	public enum EnumAxis implements IStringSerializable
	{
		X("x"),
		Y("y"),
		Z("z"),
		NONE("none");

		private final String name;

		EnumAxis(final String name)
		{
			this.name = name;
		}

		@Override
		public String toString()
		{
			return name;
		}

		public static CubeFacingXYZ.EnumAxis fromFacingAxis(final EnumFacing.Axis axis)
		{
			switch (axis)
			{
			case X:
				return X;
			case Y:
				return Y;
			case Z:
				return Z;
			default:
				return NONE;
			}
		}

		@Override
		public String getName()
		{
			return name;
		}
	}


	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, CUBE_AXIS);
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	@Override
	public IBlockState getStateForPlacement(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer)
	{
		return getStateFromMeta(meta).withProperty(CUBE_AXIS, CubeFacingXYZ.EnumAxis.fromFacingAxis(facing.getAxis()));
	}

	@Override
	public IBlockState getStateFromMeta(final int meta) {
		// TODO Auto-generated method stub
		return getDefaultState().withProperty(CUBE_AXIS, meta == 1 ? EnumAxis.X : meta == 2 ? EnumAxis.Y : meta == 3 ? EnumAxis.Z : EnumAxis.NONE);
	}

	@Override
	public int getMetaFromState(final IBlockState state) {

		EnumAxis type = state.getValue(CUBE_AXIS);

		if(type.getName().equals("none")) return 0;
		if(type.getName().equals("x")) return 1;
		if(type.getName().equals("y")) return 2;
		if(type.getName().equals("z")) return 3;

		return 0;
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withRotation(final IBlockState state, final Rotation rot)
	{
		switch (rot)
		{
		case COUNTERCLOCKWISE_90:
		case CLOCKWISE_90:

			switch (state.getValue(CUBE_AXIS))
			{
			case X:
				return state.withProperty(CUBE_AXIS, CubeFacingXYZ.EnumAxis.Z);
			case Z:
				return state.withProperty(CUBE_AXIS, CubeFacingXYZ.EnumAxis.X);
			default:
				return state;
			}

		default:
			return state;
		}
	}
}
