package mod.sfhcore.items;

import mod.sfhcore.blocks.CustomDoor;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemDoor extends Item
{

	public ItemDoor()
	{
		maxStackSize = 1;
	}

	@Override
	public EnumActionResult onItemUse(final EntityPlayer player, final World worldIn, BlockPos pos, final EnumHand hand, final EnumFacing facing, final float hitX, final float hitY, final float hitZ)
	{
		if (facing != EnumFacing.UP)
			return EnumActionResult.FAIL;
		else
		{
			final IBlockState iblockstate = worldIn.getBlockState(pos);
			final Block block = iblockstate.getBlock();

			if (!block.isReplaceable(worldIn, pos))
				pos = pos.offset(facing);

			final ItemStack itemstack = player.getHeldItem(hand);

			final Block door = Block.REGISTRY.getObject(getRegistryName());

			if (player.canPlayerEdit(pos, facing, itemstack) && door.canPlaceBlockAt(worldIn, pos))
			{
				final EnumFacing enumfacing = EnumFacing.fromAngle(player.rotationYaw);
				final int i = enumfacing.getFrontOffsetX();
				final int j = enumfacing.getFrontOffsetZ();
				final boolean flag = i < 0 && hitZ < 0.5F || i > 0 && hitZ > 0.5F || j < 0 && hitX > 0.5F || j > 0 && hitX < 0.5F;
				placeDoor(worldIn, pos, enumfacing, door, flag);
				final SoundType soundtype = worldIn.getBlockState(pos).getBlock().getSoundType(worldIn.getBlockState(pos), worldIn, pos, player);
				worldIn.playSound(player, pos, soundtype.getPlaceSound(), SoundCategory.BLOCKS, (soundtype.getVolume() + 1.0F) / 2.0F, soundtype.getPitch() * 0.8F);
				itemstack.shrink(1);
				return EnumActionResult.SUCCESS;
			} else
				return EnumActionResult.FAIL;
		}
	}

	public static void placeDoor(final World worldIn, final BlockPos pos, final EnumFacing facing, final Block door, boolean isRightHinge)
	{

		final BlockPos blockpos = pos.offset(facing.rotateY());
		final BlockPos blockpos1 = pos.offset(facing.rotateYCCW());
		final int i = (worldIn.getBlockState(blockpos1).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos1.up()).isNormalCube() ? 1 : 0);
		final int j = (worldIn.getBlockState(blockpos).isNormalCube() ? 1 : 0) + (worldIn.getBlockState(blockpos.up()).isNormalCube() ? 1 : 0);
		final boolean flag = worldIn.getBlockState(blockpos1).getBlock() == door || worldIn.getBlockState(blockpos1.up()).getBlock() == door;
		final boolean flag1 = worldIn.getBlockState(blockpos).getBlock() == door || worldIn.getBlockState(blockpos.up()).getBlock() == door;

		if ((!flag || flag1) && j <= i)
		{
			if (flag1 && !flag || j < i)
				isRightHinge = false;
		} else
			isRightHinge = true;

		final BlockPos blockpos2 = pos.up();
		final boolean flag2 = worldIn.isBlockPowered(pos) || worldIn.isBlockPowered(blockpos2);
		final IBlockState iblockstate = door.getDefaultState().withProperty(CustomDoor.FACING, facing).withProperty(CustomDoor.HINGE, isRightHinge ? CustomDoor.EnumHingePosition.RIGHT : CustomDoor.EnumHingePosition.LEFT).withProperty(CustomDoor.POWERED, flag2).withProperty(CustomDoor.OPEN, flag2);
		worldIn.setBlockState(pos, iblockstate.withProperty(CustomDoor.HALF, CustomDoor.EnumDoorHalf.LOWER), 2);
		worldIn.setBlockState(blockpos2, iblockstate.withProperty(CustomDoor.HALF, CustomDoor.EnumDoorHalf.UPPER), 2);
		worldIn.notifyNeighborsOfStateChange(pos, door, false);
		worldIn.notifyNeighborsOfStateChange(blockpos2, door, false);
	}
}