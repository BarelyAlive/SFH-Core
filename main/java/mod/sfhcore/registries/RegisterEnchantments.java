package mod.sfhcore.registries;

import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.util.NonNullList;
import net.minecraftforge.registries.IForgeRegistry;

public class RegisterEnchantments
{
	private static NonNullList<Enchantment> enchantments = NonNullList.create();

	public static List<Enchantment> getEnchantments() {
		return enchantments;
	}

	public static void registerEnchantments(final IForgeRegistry<Enchantment> registry)
	{
		for(Enchantment chant : enchantments)
			registry.register(chant);
	}
}
