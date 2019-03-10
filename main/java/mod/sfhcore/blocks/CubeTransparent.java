package mod.sfhcore.blocks;

import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class CubeTransparent extends Cube{
	
	public CubeTransparent(Material material, float resistance, float hardness, int sub, String name) {
		super(material, resistance, hardness, sub, name);
		setLightOpacity(15);
	}
	
	public CubeTransparent(Material material, float resistance, float hardness, int sub, String name, TileEntity te) {
		super(material, resistance, hardness, sub, name, te);
		setLightOpacity(15);
	}
	
	public int quantityDropped(Random random)
    {
        return 1;
    }

    @SideOnly(Side.CLIENT)
    public BlockRenderLayer getBlockLayer()
    {
        return BlockRenderLayer.CUTOUT;
    }

    public boolean isFullCube(IBlockState state)
    {
        return false;
    }

    protected boolean canSilkHarvest()
    {
        return true;
    }
}
