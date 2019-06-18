package mod.sfhcore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Cube extends Block
{		
	private TileEntity te;
	
	public Cube(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc) {
		this(material, resistance, hardness, tab, loc, null);
	}
	
	public Cube(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc, TileEntity te) {
		super(material);
		setCreativeTab(tab);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		setRegistryName(loc);
		this.te = te;
		if(material == Material.GROUND){
			setSoundType(blockSoundType.GROUND);;
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
			setSoundType(blockSoundType.GROUND);
		}
		if(material == Material.WOOD){
			setSoundType(blockSoundType.WOOD);
		}
	}
	
	@Override
	public TileEntity createTileEntity(World world, IBlockState state) {
		return this.te;
	}
}
