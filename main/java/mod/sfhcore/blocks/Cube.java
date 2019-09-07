package mod.sfhcore.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class Cube extends Block
{
	private TileEntity te;

	public Cube(final Material material, final float resistance, final float hardness, final CreativeTabs tab, final ResourceLocation loc) {
		this(material, resistance, hardness, tab, loc, null);
	}

	public Cube(final Material material, final float resistance, final float hardness, final CreativeTabs tab, final ResourceLocation loc, final TileEntity te) {
		super(material);
		setCreativeTab(tab);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		setRegistryName(loc);
		this.te = te;
		if(material == Material.GROUND){
			setSoundType(SoundType.GROUND);
		}
		if(material == Material.ROCK)
			setSoundType(SoundType.STONE);
		if(material == Material.SAND)
			setSoundType(SoundType.SAND);
		if(material == Material.GLASS)
			setSoundType(SoundType.GLASS);
		if(material == Material.GRASS)
			setSoundType(SoundType.GROUND);
		if(material == Material.WOOD)
			setSoundType(SoundType.WOOD);
	}

	@Override
	public TileEntity createTileEntity(final World world, final IBlockState state) {
		return te;
	}
}
