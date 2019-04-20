package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterEnchantments
{
	public static List<Enchantment> enchantments = new ArrayList<Enchantment>();
	
	public static void registerEnchantments(IForgeRegistry<Enchantment> registry)
	{
		ResourceLocation loc;
		
		for(Enchantment chant : enchantments)
		{
			if (chant != null && chant.getRegistryName() != null)
			{
				registry.register(chant);
			}
		}
	}
}