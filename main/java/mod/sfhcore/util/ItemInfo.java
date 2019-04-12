package mod.sfhcore.util;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.oredict.OreDictionary;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class ItemInfo
{
    
    private Item item;
    private int meta;
    
    public int getMeta() {
		return meta;
	}

	public void setMeta(int meta) {
		this.meta = meta;
	}

	public Item getItem() {
		return item;
	}

	public static ItemInfo getItemInfoFromStack(ItemStack stack)
    {
        return new ItemInfo(stack);
    }
    
    public ItemInfo(ItemStack stack)
    {
        item = stack == null ? null : stack.getItem();
        meta = stack == null ? -1 : stack.getMetadata();
    }
    
    public ItemInfo(Item item, int blockMeta)
    {
        this.item = item;
        meta = item == null ? -1 : blockMeta;
    }
    
    public ItemInfo(Block block, int blockMeta)
    {
        item = Item.getItemFromBlock(block);
        meta = block == null ? -1 : blockMeta;
    }
    
    public ItemInfo(String string)
    {
        String[] split = string.split(":");
        
        if(split.length == 1)
        {
            item = Item.getByNameOrId("minecraft:" + split[0]);
        }
        else if(split.length == 2)
        {
            try
            {
                meta = split[1].equals("*") ? -1 : Integer.parseInt(split[1]);
                item = Item.getByNameOrId("minecraft:" + split[0]);
            }
            catch(NumberFormatException e)
            {
                meta = -1;
                item = Item.getByNameOrId(split[0] + ":" + split[1]);
            }
        }
        else if(split.length == 3)
        {
            try
            {
                meta = split[2].equals("*") ? -1 : Integer.parseInt(split[2]);
                item = Item.getByNameOrId(split[0] + ":" + split[1]);
            }
            catch(NumberFormatException e)
            {
                meta = -1;
            }
        }
        else
        {
            meta = -1;
        }
    }
    
    public ItemInfo(IBlockState state)
    {
        item = state == null ? null : Item.getItemFromBlock(state.getBlock());
        meta = state == null ? -1 : state.getBlock().getMetaFromState(state);
    }
    
    public String toString()
    {
        return Item.REGISTRY.getNameForObject(item) + (meta == -1 ? "" : (":" + meta));
    }
    
    public ItemStack getItemStack()
    {
        return item == null ? null : new ItemStack(item, 1, meta == -1 ? 0 : meta);
    }
    
    public NBTTagCompound writeToNBT(NBTTagCompound tag)
    {
        tag.setString("item", Item.REGISTRY.getNameForObject(item).toString());
        tag.setInteger("meta", meta);
        
        return tag;
    }
    
    public static ItemInfo readFromNBT(NBTTagCompound tag)
    {
        Item item_ = Item.REGISTRY.getObject(new ResourceLocation(tag.getString("item")));
        int meta_ = tag.getInteger("meta");
        
        return new ItemInfo(item_, meta_);
    }
    
    public int hashCode()
    {
        return item == null ? 41 : item.hashCode();
    }
    
    public boolean equals(Object other)
    {
        if (other instanceof ItemInfo)
        {
            ItemInfo info = (ItemInfo) other;
            
            if(item == null || info.item == null)
            {
                return false;
            }
            
            if (meta == -1 || info.meta == -1)
            {
                return item.equals(info.item);
            }
            else
            {
                return  meta == info.meta && item.equals(info.item);
            }
        }
        
        return false;
    }
}