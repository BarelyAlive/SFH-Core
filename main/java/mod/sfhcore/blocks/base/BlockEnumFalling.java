package mod.sfhcore.blocks.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;

public class BlockEnumFalling<E extends Enum<E> & IStringSerializable> extends BlockFalling implements IVariantProvider
{
    private final E[] types;
    private final PropertyEnum<E> property;
    private final BlockStateContainer realStateContainer;

    public BlockEnumFalling(Material material, Class<E> enumClass, String propName, ResourceLocation loc, float resi, float hard)
    {
        super(material);

        this.types = enumClass.getEnumConstants();
        this.property = PropertyEnum.create(propName, enumClass);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
        setRegistryName(loc);
        setResistance(resi);
        setHardness(hard);
    }
    
    @Override
    public ItemStack getPickBlock(IBlockState state, RayTraceResult target, World world, BlockPos pos,
    		EntityPlayer player)
    {
    	return new ItemStack(Item.getItemFromBlock(this), 1, this.getMetaFromState(world.getBlockState(pos)));
    }
    
    public E[] getTypes(){
    	return this.types;
    }

    public BlockEnumFalling(Material material, Class<E> enumClass, ResourceLocation loc, float resi, float hard)
    {
        this(material, enumClass, "type", loc, resi, hard);
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
