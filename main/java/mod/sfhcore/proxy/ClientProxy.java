package mod.sfhcore.proxy;

import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.Constants;
import mod.sfhcore.helper.FluidStateMapper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{

	private final String INVENTORY = "inventory";
	
	@Override
	@SideOnly(Side.CLIENT)
	public void initModel(final Fluid f, final Block b)
	{
		FluidStateMapper mapper = new FluidStateMapper(b.getRegistryName().getResourceDomain(), f);

		Item item = Item.getItemFromBlock(b);
		if (item != null) {
			ModelBakery.registerItemVariants(item);
			ModelLoader.setCustomMeshDefinition(item, mapper);
		}

		ModelLoader.setCustomStateMapper(b, mapper);
	}
}