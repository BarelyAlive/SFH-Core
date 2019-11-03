package mod.sfhcore.proxy;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
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
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.Constants;

public class SFHCoreClientProxy extends SFHCoreProxy{
    
    public static void tryHandleBlockModel(Block block, String name, int no, String modid)
    {
        if (no > 0)
        {
        	for (int i = 0; i < no; i ++) {
                ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), i, new ModelResourceLocation(new ResourceLocation(modid, name + "_" + i), "normal"));
        	}
        }else{
        	ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(block), 0, new ModelResourceLocation(new ResourceLocation(modid, name), "normal"));
        }
    }
    
    public static void tryHandleItemModel(Item item, String name, int no, String modid)
    {
        if(!item.getHasSubtypes()){
        	ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(modid + ":" + name, "inventory"));
        }else if(item.getHasSubtypes()){
        	for (int i = 0; i < no; i ++) {
    			ModelLoader.setCustomModelResourceLocation(item, i, new ModelResourceLocation(modid + ":" + name + "_" + i, "inventory"));
    		}
        }
    }
}