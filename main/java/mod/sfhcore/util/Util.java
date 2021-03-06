package mod.sfhcore.util;

import static java.lang.Math.round;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.function.BiPredicate;

import javax.annotation.Nonnull;

import mod.sfhcore.texturing.Color;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

public class Util {

	public static final Color whiteColor = new Color(1f, 1f, 1f, 1f);
	public static final Color blackColor = new Color(0f, 0f, 0f, 1f);
	public static final Color greenColor = new Color(0f, 1f, 0f, 1f);

	public static void dropItemInWorld(final TileEntity source, final EntityPlayer player, final ItemStack stack, final double speedFactor) {
		if (stack == null || stack.isEmpty())
			return;

		final int hitOrientation = player == null ? 0 : MathHelper.floor(player.rotationYaw * 4.0F / 360.0F + 0.5D) & 3;
		double stackCoordX = 0.0D, stackCoordY = 0.0D, stackCoordZ = 0.0D;

		switch (hitOrientation) {
		case 0:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() - 0.25D;
			break;
		case 1:
			stackCoordX = source.getPos().getX() + 1.25D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		case 2:
			stackCoordX = source.getPos().getX() + 0.5D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 1.25D;
			break;
		case 3:
			stackCoordX = source.getPos().getX() - 0.25D;
			stackCoordY = source.getPos().getY() + 0.5D + 1;
			stackCoordZ = source.getPos().getZ() + 0.5D;
			break;
		default:
			break;
		}

		final EntityItem droppedEntity = new EntityItem(source.getWorld(), stackCoordX, stackCoordY, stackCoordZ, stack);

		if (player != null) {
			final Vec3d motion = new Vec3d(player.posX - stackCoordX, player.posY - stackCoordY, player.posZ - stackCoordZ);
			motion.normalize();
			droppedEntity.motionX = motion.x;
			droppedEntity.motionY = motion.y;
			droppedEntity.motionZ = motion.z;
			final double offset = 0.25D;
			droppedEntity.move(MoverType.SELF, motion.x * offset, motion.y * offset, motion.z * offset);
		}

		droppedEntity.motionX *= speedFactor;
		droppedEntity.motionY *= speedFactor;
		droppedEntity.motionZ *= speedFactor;

		source.getWorld().spawnEntity(droppedEntity);
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTextureFromBlockState(@Nonnull final IBlockState state) {
		return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(state);
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTextureFromFluidStack(final FluidStack stack) {
		if (stack != null && stack.getFluid() != null)
			return getTextureFromFluid(stack.getFluid());

		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTextureFromFluid(@Nonnull final Fluid fluid) {
		TextureAtlasSprite tex = null;
		// Try still
		if (fluid.getStill() != null)
			tex = getTexture(fluid.getStill());
		if(tex != null)
			return tex;
		// Try flowing
		if (fluid.getFlowing() != null)
			tex = getTexture(fluid.getFlowing());
		if(tex != null)
			return tex;
		// Try grabbing the block
		if (fluid.getBlock() != null)
			tex = getTextureFromBlockState(fluid.getBlock().getDefaultState());
		if(tex != null)
			return tex;
		// Give up
		return Minecraft.getMinecraft().getTextureMapBlocks().getMissingSprite();
	}

	@SideOnly(Side.CLIENT)
	public static TextureAtlasSprite getTexture(final ResourceLocation location) {
		return Minecraft.getMinecraft().getTextureMapBlocks().getTextureExtry(location.toString());
	}

	public static boolean isSurroundingBlocksAtLeastOneOf(final BlockInfo[] blocks, final BlockPos pos, final World world, final int radius) {
		final ArrayList<BlockInfo> blockList = new ArrayList<>(Arrays.asList(blocks));
		for (int xShift = -1 * radius; xShift <= radius; xShift++)
			for (int zShift = -1 * radius; zShift <= radius; zShift++) {
				final BlockPos checkPos = pos.add(xShift, 0, zShift);
				final BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
				if (blockList.contains(checkBlock))
					return true;
			}

		return false;
	}

	public static int getNumSurroundingBlocksAtLeastOneOf(final BlockInfo[] blocks, final BlockPos pos, final World world) {

		int ret = 0;
		final ArrayList<BlockInfo> blockList = new ArrayList<>(Arrays.asList(blocks));
		for (int xShift = -2; xShift <= 2; xShift++)
			for (int zShift = -2; zShift <= 2; zShift++) {
				final BlockPos checkPos = pos.add(xShift, 0, zShift);
				final BlockInfo checkBlock = new BlockInfo(world.getBlockState(checkPos));
				if (blockList.contains(checkBlock))
					ret++;
			}


		return ret;
	}

	public static int getLightValue(final FluidStack fluid) {
		if (fluid != null && fluid.getFluid() != null)
			return fluid.getFluid().getLuminosity(fluid);
		else
			return 0;
	}

	public static float weightedAverage(final float a, final float b, final float percent) {
		return a * percent + b * (1 - percent);
	}

	public static ItemStack getBucketStack(final Fluid fluid) {
		return FluidUtil.getFilledBucket(new FluidStack(fluid, 1000));
	}

	public static boolean compareItemStack(final ItemStack stack1, final ItemStack stack2) {
		if (stack1.getMetadata() == OreDictionary.WILDCARD_VALUE || stack2.getMetadata() == OreDictionary.WILDCARD_VALUE)
			return stack1.getItem() == stack2.getItem();
		else return stack1.getItem() == stack2.getItem() && stack1.getMetadata() == stack2.getMetadata();
	}

	public static int interpolate(final int low, final int high, final float amount) {
		if (amount > 1.0f) return high;
		if (amount < 0.0f) return low;
		return low + round((high - low) * amount);
	}

	public static boolean isLeaves(final IBlockState state) {
		final ItemStack itemStack = new ItemStack(state.getBlock());
		return OreDictionary.getOres("treeLeaves").stream().anyMatch(stack1 -> Util.compareItemStack(stack1, itemStack));
	}

	public static <T, U>boolean arrayEqualsPredicate(final T[] a, final U[] a2, final BiPredicate<T, U> predicate) {
		if (a==a2)
			return true;

		if (a==null || a2==null)
			return false;

		final int length = a.length;
		if (a2.length != length)
			return false;

		for (int i=0; i<length; i++) {
			final T o1 = a[i];
			final U o2 = a2[i];

			if (!(o1==null ? o2==null : predicate.test(o1, o2)))
				return false;
		}

		return true;
	}

	/**
	 * A slow simulation of incrementing a counter until toMatch is reached. Used to avoid floating point errors.
	 *
	 * @param toMatch total value to reach
	 * @param stepSize increment size
	 * @return Number of times stepSize needs to be added to reach toMatch
	 */
	public static int stepsRequiredToMatch(final float toMatch, final float stepSize) {
		int n = 0;
		float accumulated = 0.0f;
		while(accumulated < toMatch) {
			accumulated += stepSize;
			n++;
		}
		return n;
	}
}