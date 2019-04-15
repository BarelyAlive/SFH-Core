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
	
	protected String name;
	int sub;
	
	public CubeFalling(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc)
	{
		this(0, material, resistance, hardness, tab, loc);
	}
	
	public CubeFalling(int sub, Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc) {
		super();
		this.name = loc.getResourcePath();
		setCreativeTab(tab);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		setUnlocalizedName(name);
		setRegistryName(loc);
		this.sub = sub;
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
	
	@Override
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
		if(itemIn.equals(this.getCreativeTabToDisplayOn()))
		{
			for (int i = 0; i < sub; i ++) {
		        items.add(new ItemStack(this, 1, i));
		    }
		}
    }
	
	@Override
    public String getUnlocalizedName() {
		int meta = this.getMetaFromState(getDefaultState());
		if(meta > 1)
		{
			return name + "_" + this.getMetaFromState(getDefaultState());
		}
		else
		{
			return name;
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
