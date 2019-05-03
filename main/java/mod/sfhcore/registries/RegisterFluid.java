package mod.sfhcore.registries;

import mod.sfhcore.helper.FluidStateMapper;
import mod.sfhcore.helper.NameHelper;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterFluid extends NameHelper
{
	public static void register(Fluid f, Block b)
	{
		f.setUnlocalizedName(f.getName());
		FluidRegistry.registerFluid(f);
		initModel(f, b);
		Registry.registerBlock(b);
		FluidRegistry.addBucketForFluid(f);
	}
	
	@SideOnly(Side.CLIENT)
	private static void initModel(Fluid f, Block b)
	{		
		FluidStateMapper mapper = new FluidStateMapper(getModID(b), f);
		
		Item item = Item.getItemFromBlock(b);
		if (item != null) {
			ModelLoader.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}
		
		ModelLoader.setCustomStateMapper(b, mapper);		
	}
}
