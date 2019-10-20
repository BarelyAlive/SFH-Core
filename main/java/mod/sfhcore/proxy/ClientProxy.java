package mod.sfhcore.proxy;

import java.util.Objects;

import mod.sfhcore.helper.FluidStateMapper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.item.Item;
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
		FluidStateMapper mapper = new FluidStateMapper(Objects.requireNonNull(b.getRegistryName()).getResourceDomain(), f);

		Item item = Item.getItemFromBlock(b);
        ModelBakery.registerItemVariants(item);
        ModelLoader.setCustomMeshDefinition(item, mapper);

        ModelLoader.setCustomStateMapper(b, mapper);
	}
}