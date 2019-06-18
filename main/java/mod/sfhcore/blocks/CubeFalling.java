package mod.sfhcore.blocks;

import net.minecraft.block.BlockFalling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.util.EnumBlockRenderType;
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
			setSoundType(SoundType.GROUND);
		}
		if(material == Material.ROCK){
			setSoundType(SoundType.STONE);
		}
		if(material == Material.SAND){
			setSoundType(SoundType.SAND);
		}
		if(material == Material.GLASS){
			setSoundType(SoundType.GLASS);
		}
		if(material == Material.GRASS){
			setSoundType(SoundType.PLANT);
		}
		if(material == Material.WOOD){
			setSoundType(SoundType.WOOD);
		}
	}
	
	/**
     * The type of render function called. 3 for standard block models, 2 for TESR's, 1 for liquids, -1 is no render
     */
    @Override
	public EnumBlockRenderType getRenderType(IBlockState state)
    {
        return EnumBlockRenderType.MODEL;
    }
}
