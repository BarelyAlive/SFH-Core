package mod.sfhcore.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemMeshDefinition;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.obj.OBJLoader;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.Constants;

public class SFHCoreClientProxy extends SFHCoreProxy{
    
	@Override
	public void tryHandleBlockModel(ResourceLocation loc)
    {
        if (loc instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) loc;
            if (variantProvider.getVariants().size() == 1)
            {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(loc), 0, new ModelResourceLocation(loc, "inventory"));
            }
            else
            {
	            for (Pair<Integer, String> variant : variantProvider.getVariants())
	            {
	                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(loc), variant.getLeft(), new ModelResourceLocation(loc, variant.getRight()));
	            }
            }
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(loc), 0, new ModelResourceLocation(loc, "inventory"));
        }
    }

	@Override
    public void tryHandleItemModel(ResourceLocation loc)
    {
        if (item instanceof IVariantProvider)
        {
            IVariantProvider variantProvider = (IVariantProvider) item;
            if (variantProvider.getVariants().size() == 1)
            {
                ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "inventory"));
            }
            else
            {
	            for (Pair<Integer, String> variant : variantProvider.getVariants())
	            {
	                ModelLoader.setCustomModelResourceLocation(item, variant.getLeft(), new ModelResourceLocation(modid + ":" + name + "_" + variant.getLeft(), "inventory"));
	            }
            }
        }
        else
        {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(loc, "inventory"));
        }
    }
}