package mod.sfhcore.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class Cube extends Block{
	
	static String name;
	int sub;
	
	public Cube(Material material, float resistance, float hardness, int sub, String name) {
		super(material);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		this.name = name;
		this.sub = sub;
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
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
    	for (int i = 0; i < sub; i ++) {
            items.add(new ItemStack(this, 1, i));
        }
    }

	@Override
    public String getUnlocalizedName() {
    	return name + "_" + this.getMetaFromState(getDefaultState());
    }
}
