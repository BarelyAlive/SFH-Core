package mod.sfhcore.blocks.itemblocks;

import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.blocks.base.BlockEnumFalling;
import mod.sfhcore.handler.CustomFuelHandler;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.math.MathHelper;

public class ItemBlockEnumFalling<E extends Enum<E> & IStringSerializable> extends ItemBlock
{

    public ItemBlockEnumFalling(BlockEnumFalling<E> block)
    {
        super(block);

        setHasSubtypes(true);
        setRegistryName(block.getRegistryName());
        setUnlocalizedName(block.getRegistryName().getResourcePath());
        setCreativeTab(block.getCreativeTabToDisplayOn());
    }
    
    @Override
	public int getItemBurnTime(ItemStack itemStack) {
		for(Pair<ItemStack, Integer> f : CustomFuelHandler.fuelList)
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
        return getBlock().getUnlocalizedName() + getBlock().getTypes()[MathHelper.clamp(stack.getItemDamage(), 0, 15)].getName();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}