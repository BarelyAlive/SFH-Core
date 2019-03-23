package mod.sfhcore.helper;

import mod.sfhcore.Constants;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.block.statemap.StateMapperBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.client.model.ModelLoader;

public class InventoryRenderHelper {

	private static String domain;
	
	public InventoryRenderHelper(String domain)
    {
        this.domain = domain;
    }
	
	public static Item getItemFromBlock(Block block)
    {
        return Item.getItemFromBlock(block);
    }
	
	public static void fluidRender(Block block, String modid)
    {
		
        final Block toRender = block;

        ModelBakery.registerItemVariants(InventoryRenderHelper.getItemFromBlock(block));
        ModelLoader.setCustomMeshDefinition(InventoryRenderHelper.getItemFromBlock(block), new ItemMeshDefinition()
        {
            @Override
            public ModelResourceLocation getModelLocation(ItemStack stack)
            {
                return new ModelResourceLocation(modid + ":" + toRender.getUnlocalizedName(), "fluid");
            }
        });
        ModelLoader.setCustomStateMapper(block, new StateMapperBase()
        {
            @Override
            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
            {
                return new ModelResourceLocation(modid + ":" + toRender.getUnlocalizedName(), "fluid");
            }
        });
    }
}
