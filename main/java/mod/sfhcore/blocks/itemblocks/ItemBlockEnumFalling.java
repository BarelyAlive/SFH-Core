package mod.sfhcore.blocks.itemblocks;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.blocks.base.BlockEnumFalling;
import mod.sfhcore.handler.CustomFuelHandler;
import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class ItemBlockEnumFalling<E extends Enum<E> & IStringSerializable> extends ItemBlock implements IVariantProvider
{

    public ItemBlockEnumFalling(Block block, CreativeTabs tab)
    {
        super((BlockEnumFalling<E>) block);

        setHasSubtypes(true);
        setRegistryName(((BlockEnumFalling<E>) block).getRegistryName());
        setCreativeTab(tab);
    }
    
    @Override
    public void getSubItems(CreativeTabs tab, NonNullList<ItemStack> items) {
    	if (tab.equals(this.getCreativeTab())) {
			for (int i = 0; i < getBlock().getTypes().length; i++) {
				items.add(new ItemStack(this, 1, i));
			} 
		}
    }
    
    @Override
	public int getItemBurnTime(ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.getFuelList())
		{
			if(ItemStack.areItemsEqual(itemStack, f.getLeft()))
			{	
				return f.getRight();	
			}
		}
		return 0;
	}

    @Override
    public BlockEnumFalling<E> getBlock()
    {
        return (BlockEnumFalling<E>) super.getBlock();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return "item." + getBlock().getRegistryName().getResourcePath() + "_" + getBlock().getTypes()[stack.getItemDamage()].toString();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
    
    @Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
        for (int i = 0; i < this.getBlock().getTypes().length; i++)
            ret.add(new ImmutablePair<Integer, String>(i, "type=" + this.getBlock().getTypes()[i]));
        return ret;
    }
}