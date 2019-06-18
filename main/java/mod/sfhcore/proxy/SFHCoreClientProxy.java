 package mod.sfhcore.proxy;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;

public class SFHCoreClientProxy extends SFHCoreProxy{
    
	@Override
	public void tryHandleBlockModel(Block block, ResourceLocation loc)
    {
        if (block instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) block;
            
            for (Pair<Integer, String> variant : variantProvider.getVariants())
            {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
            }
        }
        else
        {
			ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(loc, "inventory"));
        }
    }
	
	@Override
	public void tryHandleBlockModel(ItemBlock block, ResourceLocation loc)
    {
        if (block instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) block;
            
            for (Pair<Integer, String> variant : variantProvider.getVariants())
            {
                ModelLoader.setCustomModelResourceLocation(block, variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
            }
        }
        else
        {
			ModelLoader.setCustomModelResourceLocation(block, 0, new ModelResourceLocation(loc, "inventory"));
        }
    }

	@Override
    public void tryHandleItemModel(Item item, ResourceLocation loc)
    {
        if (item instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) item;
            
            for (Pair<Integer, String> variant : variantProvider.getVariants())
            {
                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
            }
        }
        else
        {
            if (!item.getHasSubtypes()) {
				ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "inventory"));
			}
        }
    }	
}