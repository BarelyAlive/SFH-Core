package mod.sfhcore.proxy;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ClientProxy extends CommonProxy{

	private final String INVENTORY = "inventory";

	@Override
	@SideOnly(Side.CLIENT)
	public void tryHandleBlockModel(final Block block, final ResourceLocation loc)
	{
		if (block instanceof IVariantProvider)
		{
			IVariantProvider variantProvider = (IVariantProvider) block;

			for (Pair<Integer, String> variant : variantProvider.getVariants())
				ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
		} else
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(loc, INVENTORY));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void tryHandleBlockModel(final ItemBlock block, final ResourceLocation loc)
	{
		if (block instanceof IVariantProvider)
		{
			IVariantProvider variantProvider = (IVariantProvider) block;

			for (Pair<Integer, String> variant : variantProvider.getVariants())
				ModelLoader.setCustomModelResourceLocation(block, variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
		} else
			ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(loc, INVENTORY));
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void tryHandleItemModel(final Item item, final ResourceLocation loc)
	{
		if (item instanceof IVariantProvider)
		{
			IVariantProvider variantProvider = (IVariantProvider) item;

			for (Pair<Integer, String> variant : variantProvider.getVariants())
				ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
		} else if (!item.getHasSubtypes())
			ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, INVENTORY));
	}
}