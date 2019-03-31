package mod.sfhcore.blocks.base;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockEnumFalling<E extends Enum<E> & IStringSerializable> extends BlockFalling implements IVariantProvider
{
    private final E[] types;
    private final PropertyEnum<E> property;
    private final BlockStateContainer realStateContainer;

    public BlockEnumFalling(Material material, Class<E> enumClass, String propName, String name, CreativeTabs tab)
    {
        super(material);

        this.types = enumClass.getEnumConstants();
        this.property = PropertyEnum.create(propName, enumClass);
        this.realStateContainer = createStateContainer();
        setDefaultState(getBlockState().getBaseState());
        setUnlocalizedName(name);
        setCreativeTab(tab);
    }
    
    public E[] getTypes(){
    	return this.types;
    }

    public BlockEnumFalling(Material material, Class<E> enumClass, String name, CreativeTabs tab)
    {
        this(material, enumClass, "type", name, tab);
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
        for (E type : types)
            items.add(new ItemStack(this, 1, type.ordinal()));
    }

    /*
    @SideOnly(Side.CLIENT)
    @Override
    public void getSubBlocks(Item item, CreativeTabs tab, List<ItemStack> subBlocks)
    {
        for (E type : types)
            subBlocks.add(new ItemStack(item, 1, type.ordinal()));
    }
    */

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
