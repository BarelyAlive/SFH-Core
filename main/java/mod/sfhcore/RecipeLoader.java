package mod.sfhcore;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class RecipeLoader {

	static void registerOreRecipes(){

	}
	static void addOreDicNames(){
		OreDictionary.registerOre("stickWood", new ItemStack(Items.STICK));
    	OreDictionary.registerOre("cobblestone", new ItemStack(Blocks.COBBLESTONE));
    	OreDictionary.registerOre("stone", new ItemStack(Blocks.STONE));
    	OreDictionary.registerOre("waterBucket", new ItemStack(Items.WATER_BUCKET));

	}
}
