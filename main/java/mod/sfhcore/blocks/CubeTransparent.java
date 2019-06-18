package mod.sfhcore.blocks;

import java.util.Random;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CubeTransparent extends Cube{
	
	public CubeTransparent(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc) {
		super(material, resistance, hardness, tab, loc);
		setLightOpacity(15);
	}
	
	public CubeTransparent(Material material, float resistance, float hardness, CreativeTabs tab, ResourceLocation loc, TileEntity te) {
		super(material, resistance, hardness, tab, loc, te);
		setLightOpacity(15);
	}
	
	@Override
	public int quantityDropped(Random random)
    {
        return 1;
    }

    @Override
	@SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    @Override
	public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    @Override
	protected boolean canSilkHarvest()
    {
        return true;
    }
}
