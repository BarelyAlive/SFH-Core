package mod.sfhcore.blocks.itemblocks;

import mod.sfhcore.blocks.base.BlockEnumFalling;
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
    }

    @SuppressWarnings("unchecked")
    @Override
    public BlockEnumFalling<E> getBlock()
    {
        return (BlockEnumFalling<E>) super.getBlock();
    }

    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return getBlock().getUnlocalizedName() + getBlock().getTypes()[MathHelper.clamp_int(stack.getItemDamage(), 0, 15)].getName();
    }

    @Override
    public int getMetadata(int damage)
    {
        return damage;
    }
}