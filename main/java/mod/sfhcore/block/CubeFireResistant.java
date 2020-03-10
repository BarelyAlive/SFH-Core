package mod.sfhcore.block;

import net.minecraft.block.material.Material;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class CubeFireResistant extends Cube{

	public CubeFireResistant(final Material material, final float resistance, final float hardness) {
		super(material, resistance, hardness);
	}

	@Override
	public boolean isFlammable(final IBlockAccess world, final BlockPos pos, final EnumFacing face) {
		return false;
	}
}
