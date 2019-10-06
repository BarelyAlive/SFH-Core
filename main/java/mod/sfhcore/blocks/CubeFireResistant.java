package mod.sfhcore.blocks;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CubeFireResistant extends Cube{

	public CubeFireResistant(Material material, float resistance, float hardness, ResourceLocation loc) {
		super(material, resistance, hardness, loc);
	}

	@Override
	public boolean isFlammable(final IBlockAccess world, final BlockPos pos, final EnumFacing face) {
		return false;
	}
}
