package mod.sfhcore.registries;

import java.util.List;

import mod.sfhcore.fluid.Fluid;
import mod.sfhcore.helper.NameHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
import net.minecraft.util.NonNullList;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RegisterFluid extends NameHelper
{
	private static NonNullList<Fluid> fluids = NonNullList.create();
	
	public static List<Fluid> getBlocks() {
		return fluids;
	}
	
	public static void register(net.minecraftforge.fluids.Fluid f, Block b)
	{
		f.setUnlocalizedName(f.getName());
		Registry.registerBlock(b);
		FluidRegistry.addBucketForFluid(f);
	}
	
	@SideOnly(Side.CLIENT)
	public static void initModel(Fluid f, Block b)
	{
		mod.sfhcore.helper.FluidStateMapper mapper = new mod.sfhcore.helper.FluidStateMapper(getModID(b), f);
		
		Item item = Item.getItemFromBlock(b);
		if (item != null) {
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}
		
		ModelLoader.setCustomStateMapper(b, mapper);		
	}
}
