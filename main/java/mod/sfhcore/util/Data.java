package mod.sfhcore.util;

import com.google.common.collect.Lists;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.crafting.IRecipe;

import java.util.LinkedList;
import java.util.List;

public class Data {

    public static final List<Item> ITEMS = new  LinkedList<Item>();
    public static final List<Block> BLOCKS = new LinkedList<Block>();
    public static final List<IRecipe> RECIPES = new LinkedList<IRecipe>();
    public static final List<Enchantment> ENCHANTMENTS = new LinkedList<Enchantment>();

}