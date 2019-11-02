package mod.sfhcore.blocks;

import java.util.Objects;
import java.util.Random;

import mod.sfhcore.blocks.tiles.TileInventory;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockHorizontal;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyDirection;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class CubeContainerHorizontal extends BlockContainer{

	private static final PropertyDirection FACING = BlockHorizontal.FACING;
	private static boolean keepInventory;

	protected CubeContainerHorizontal(final Material materialIn) {
		super(materialIn);
		setDefaultState(blockState.getBaseState().withProperty(FACING, EnumFacing.NORTH));
	}

	/**
	 * Called by ItemBlocks after a block is set in the world, to allow post-place logic
	 */
	@Override
	public void onBlockPlacedBy(final World worldIn, final BlockPos pos, final IBlockState state, final EntityLivingBase placer, final ItemStack stack)
	{
		worldIn.setBlockState(pos, state.withProperty(FACING, placer.getHorizontalFacing().getOpposite()), 2);

		if (stack.hasDisplayName())
		{
			final TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileInventory)
				((TileInventory)tileentity).setCustomInventoryName(stack.getDisplayName());
		}
	}

	@Override
	public void breakBlock(final World worldIn, final BlockPos pos, final IBlockState state)
	{
		if (!keepInventory)
		{
			final TileEntity tileentity = worldIn.getTileEntity(pos);

			if (tileentity instanceof TileInventory)
			{
				InventoryHelper.dropInventoryItems(worldIn, pos, (TileInventory) tileentity);
				worldIn.updateComparatorOutputLevel(pos, this);
			}
		}

		super.breakBlock(worldIn, pos, state);
	}

	private void dropItems(final World world, final int x, final int y, final int z) {
		final Random rand = new Random();

		final TileEntity tileEntity = world.getTileEntity(new BlockPos(x, y, z));
		if(!(tileEntity instanceof IInventory))
			return;

		final IInventory inventory = (IInventory) tileEntity;

		for(int i = 0; i < inventory.getSizeInventory(); i++) {
			final ItemStack item = inventory.getStackInSlot(i);

			if(item.getCount() > 0) {
				final float rx = rand.nextFloat() *0.8F + 0.1F;
				final float ry = rand.nextFloat() *0.8F + 0.1F;
				final float rz = rand.nextFloat() *0.8F + 0.1F;

				final EntityItem entityItem = new EntityItem(world, x + rx, y + ry, z + rz, new ItemStack(item.getItem(), item.getCount(), item.getItemDamage()));

				if(item.hasTagCompound())
					entityItem.getItem().setTagCompound(Objects.requireNonNull(item.getTagCompound()).copy());

				final float factor = 0.05F;
				entityItem.motionX = rand.nextGaussian() * factor;
				entityItem.motionY = rand.nextGaussian() * factor + 0.2F;
				entityItem.motionZ = rand.nextGaussian() * factor;
				world.spawnEntity(entityItem);
				item.setCount(0);
			}
		}
	}

	@Override
	public boolean hasComparatorInputOverride(final IBlockState state)
	{
		return true;
	}

	@Override
	public int getComparatorInputOverride(final IBlockState blockState, final World worldIn, final BlockPos pos)
	{
		return Container.calcRedstone(worldIn.getTileEntity(pos));
	}

	//Block Facing Part

	/**
	 * Convert the given metadata into a BlockState for this Block
	 */
	@Override
	public IBlockState getStateFromMeta(final int meta)
	{
		EnumFacing enumfacing = EnumFacing.getFront(meta);

		if (enumfacing.getAxis() == EnumFacing.Axis.Y)
			enumfacing = EnumFacing.NORTH;

		return getDefaultState().withProperty(FACING, enumfacing);
	}

	/**
	 * Convert the BlockState into the correct metadata value
	 */
	@Override
	public int getMetaFromState(final IBlockState state)
	{
		return state.getValue(FACING).getIndex();
	}

	/**
	 * Returns the blockstate with the given rotation from the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withRotation(final IBlockState state, final Rotation rot)
	{
		return state.withProperty(FACING, rot.rotate(state.getValue(FACING)));
	}

	/**
	 * Returns the blockstate with the given mirror of the passed blockstate. If inapplicable, returns the passed
	 * blockstate.
	 */
	@Override
	public IBlockState withMirror(final IBlockState state, final Mirror mirrorIn)
	{
		return state.withRotation(mirrorIn.toRotation(state.getValue(FACING)));
	}

	@Override
	public void onBlockAdded(final World worldIn, final BlockPos pos, final IBlockState state)
	{
		setDefaultFacing(worldIn, pos, state);
	}

	private void setDefaultFacing(final World worldIn, final BlockPos pos, final IBlockState state)
	{
		if (!worldIn.isRemote)
		{
			final IBlockState iblockstate = worldIn.getBlockState(pos.north());
			final IBlockState iblockstate1 = worldIn.getBlockState(pos.south());
			final IBlockState iblockstate2 = worldIn.getBlockState(pos.west());
			final IBlockState iblockstate3 = worldIn.getBlockState(pos.east());
			EnumFacing enumfacing = state.getValue(FACING);

			if (enumfacing == EnumFacing.NORTH && iblockstate.isFullBlock() && !iblockstate1.isFullBlock())
				enumfacing = EnumFacing.SOUTH;
			else if (enumfacing == EnumFacing.SOUTH && iblockstate1.isFullBlock() && !iblockstate.isFullBlock())
				enumfacing = EnumFacing.NORTH;
			else if (enumfacing == EnumFacing.WEST && iblockstate2.isFullBlock() && !iblockstate3.isFullBlock())
				enumfacing = EnumFacing.EAST;
			else if (enumfacing == EnumFacing.EAST && iblockstate3.isFullBlock() && !iblockstate2.isFullBlock())
				enumfacing = EnumFacing.WEST;

			worldIn.setBlockState(pos, state.withProperty(FACING, enumfacing), 2);
		}
	}

	/**
	 * Called by ItemBlocks just before a block is actually set in the world, to allow for adjustments to the
	 * IBlockstate
	 */
	public IBlockState onBlockPlaced(final World worldIn, final BlockPos pos, final EnumFacing facing, final float hitX, final float hitY, final float hitZ, final int meta, final EntityLivingBase placer)
	{
		return getDefaultState().withProperty(FACING, placer.getHorizontalFacing().getOpposite());
	}

	//BlockFacinPart ENDE

	/**
	 * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
	 */
	@Override
	public EnumBlockRenderType getRenderType(final IBlockState state)
	{
		return EnumBlockRenderType.MODEL;
	}

	@Override
	public boolean hasTileEntity() {
		return true;
	}

	@Override
	public TileEntity createNewTileEntity(final World p_149915_1_, final int p_149915_2_) {
		return null;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		return new BlockStateContainer(this, FACING);
	}
}
