package mod.sfhcore.blocks;

import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.BlockFalling;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;

public class CubeFalling extends BlockFalling{
	
	public CubeFalling(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc) {
		super();
		setCreativeTab(tab);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		setRegistryName(loc);
		if(material == Material.GROUND){
			setSoundType(blockSoundType.GROUND);
		}
		if(material == Material.ROCK){
			setSoundType(blockSoundType.STONE);
		}
		if(material == Material.SAND){
			setSoundType(blockSoundType.SAND);
		}
		if(material == Material.GLASS){
			setSoundType(blockSoundType.GLASS);
		}
		if(material == Material.GRASS){
			setSoundType(blockSoundType.PLANT);
		}
		if(material == Material.WOOD){
			setSoundType(blockSoundType.WOOD);
		}
	}
	
	/**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
