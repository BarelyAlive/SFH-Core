package mod.sfhcore.blocks.base;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.jws.Oneway;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import mod.sfhcore.util.Util;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnum<E extends Enum<E> & IStringSerializable> extends Block implements IVariantProvider
{
    private final E[] types;
    private final PropertyEnum<E> property;
    private final BlockStateContainer realStateContainer;

    public BlockEnum(Material material, Class<E> enumClass, String propName, ResourceLocation loc, CreativeTabs tab)
    {
        super(material);

        this.types = enumClass.getEnumConstants();
        this.property = PropertyEnum.create(propName, enumClass);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
        setRegistryName(loc);
        setUnlocalizedName(loc.getResourcePath());
        setCreativeTab(tab);
    }
    
    @Override
    public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
    		ItemStack stack) {
    	worldIn.setBlockState(pos, getStateFromMeta(stack.getItemDamage()));
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
    		EntityPlayer player) {
    	return new ItemStack(state.getBlock(), 1, getMetaFromState(state));
    }
    
    public E[] getTypes(){
    	return this.types;
    }

    public BlockEnum(Material material, Class<E> enumClass, ResourceLocation loc, CreativeTabs tab)
    {
        this(material, enumClass, "type", loc, tab);
    }

    @Override
    protected final BlockStateContainer createBlockState()
    {
        return new BlockStateContainer.Builder(this).build(); // Blank to avoid crashes
    }

    @Override
    public final BlockStateContainer getBlockState()
    {
        return realStateContainer;
    }

    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        return getDefaultState().withProperty(property, types[meta]);
    }

    @Override
    public int getMetaFromState(IBlockState state)
    {
        return state.getValue(property).ordinal();
    }

    @Override
    public int damageDropped(IBlockState state)
    {
        return getMetaFromState(state);
    }
    
    @Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
    	if(itemIn.equals(this.getCreativeTabToDisplayOn()))
		{   
    		for (E type : types)
	            items.add(new ItemStack(this, 1, type.ordinal()));
		}
    }

    protected BlockStateContainer createStateContainer()
    {
        return new BlockStateContainer.Builder(this).add(property).build();
    }
    
    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < this.getTypes().length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + this.getTypes()[i]));
        return ret;
    }
}
