package mod.sfhcore.blocks;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import mod.sfhcore.proxy.IVariantProvider;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class Cube extends Block implements IVariantProvider{
	
	String name;
	int sub;
	TileEntity te;
	
	
	public Cube(Material material, float resistance, float hardness, int sub, String name) {
		this(material, resistance, hardness, sub, name, null);
	}
	
	public Cube(Material material, float resistance, float hardness, int sub, String name, TileEntity te) {
		super(material);
		setResistance(resistance);
		setHardness(hardness);
		setLightOpacity(0);
		setUnlocalizedName(name);
		this.name = name;
		this.sub = sub;
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
    public void getSubBlocks(CreativeTabs itemIn, NonNullList<ItemStack> items) {
    	for (int i = 0; i < sub; i ++) {
            items.add(new ItemStack(this, 1, i));
        }
    }
	
	@Override
    public List<Pair<Integer, String>> getVariants()
    {
        List<Pair<Integer, String>> ret = new ArrayList<Pair<Integer, String>>();
            ret.add(new ImmutablePair<Integer, String>(0, "type=normal"));
        return ret;
    }
}
