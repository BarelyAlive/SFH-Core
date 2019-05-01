package mod.sfhcore.registries;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterEnchantments
{
	private static NonNullList<Enchantment> enchantments = NonNullList.create();
	
	public static List<Enchantment> getEnchantments() {
		return enchantments;
	}

	public static void registerEnchantments(IForgeRegistry<Enchantment> registry)
	{
		ResourceLocation loc;
		
		for(Enchantment chant : enchantments)
		{
			if (chant == null)
				continue;
			if(chant.getRegistryName() == null)
				continue;
			
			registry.register(chant);
		}
	}
}
