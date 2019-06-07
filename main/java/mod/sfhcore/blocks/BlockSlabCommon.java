package mod.sfhcore.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class BlockSlabCommon extends BlockSlab implements IVariantProvider{

	public static final PropertyEnum<Variant> VARIANT = PropertyEnum.<Variant>create("variant", Variant.class);
	
	private static BlockSlabCommon halfSlab;

	private static BlockSlabCommon doubleSlab;
	
	public BlockSlabCommon(String name, Material material, BlockSlabCommon halfSlab, BlockSlabCommon doubleSlab) {
		super(material);
		this.setRegistryName(name);
		this.setUnlocalizedName(name);
		
		this.halfSlab = halfSlab;
		this.doubleSlab = doubleSlab;
		
		IBlockState iblockstate = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
		
		if(!this.isDouble()) {
			iblockstate.withProperty(HALF, BlockSlab.EnumBlockHalf.BOTTOM);
		}
		
		this.setDefaultState(iblockstate);
		this.useNeighborBrightness = !this.isDouble();
	}

	@Override
	public String getUnlocalizedName(int meta) {
		return super.getUnlocalizedName();
	}

	@Override
	public IProperty<?> getVariantProperty() {
		return VARIANT;
	}

	@Override
	public Comparable<?> getTypeForItem(ItemStack stack) {
		return Variant.DEFAULT;
	}

	@Override
	public Item getItemDropped(IBlockState state, Random rand, int fortune) {
		return Item.getItemFromBlock(halfSlab);
	}
	
	
	@Override
	public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
		return new ItemStack(doubleSlab);
	}
	
	@Override
	public final IBlockState getStateFromMeta(final int meta) {
		IBlockState blockstate = this.blockState.getBaseState().withProperty(VARIANT, Variant.DEFAULT);
		
		if(!this.isDouble()) {
			blockstate = blockstate.withProperty(HALF, ((meta&8)!=0)?EnumBlockHalf.TOP:EnumBlockHalf.BOTTOM);
		}
		
		return blockstate;
	}
	
	@Override
	public final int getMetaFromState(final IBlockState state) {
		int meta = 0;
		
		if(!this.isDouble()&& state.getValue(HALF)==EnumBlockHalf.TOP) {
			meta |= 8;
		}
		
		return meta;
	}
	
	@Override
	protected BlockStateContainer createBlockState() {
		if(!this.isDouble()){
			return new BlockStateContainer(this, new IProperty[] {VARIANT, HALF});
		}
		return new BlockStateContainer(this, new IProperty[] {VARIANT});
	}
	
	public static class Double extends BlockSlabCommon
	{

		public Double(String name, Material material) {
			super(name, material, halfSlab, doubleSlab);
		}

		@Override
		public boolean isDouble() {
			return true;
		}

	}

	public static class Half extends BlockSlabCommon
	{

		public Half(String name, Material material) {
			super(name, material, halfSlab, doubleSlab);
		}

		@Override
		public boolean isDouble() {
			return false;
		}

	}
	
	public static enum Variant implements IStringSerializable
	{
		DEFAULT;

		@Override
		public String getName() {
			return "default";
		}
	
	}
	
	@Override
	public List<Pair<Integer, String>> getVariants()
	{
		List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        if (!this.isDouble()) {
			ret.add(new ImmutablePair<Integer, String>(0, "half=bottom,variant=default"));
			ret.add(new ImmutablePair<Integer, String>(1, "half=top,variant=default"));
		}
        else
        	ret.add(new ImmutablePair<Integer, String>(0, "variant=default"));
		return ret;
	}
}