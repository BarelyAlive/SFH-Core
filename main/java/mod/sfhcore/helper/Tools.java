package mod.sfhcore.helper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class Tools {

	/**
	 * Checks a whole room for containing a single block only.
	 * Starts in the left corner on the lowest level and counts from left to right, up to down and bottom to top.
	 * 
	 * @param world
	 * @param block
	 * @param pos
	 * @param laenge
	 * @param breite
	 * @param hoehe
	 * @return
	 */
	public static boolean checkBlockArea(World world, IBlockState block, BlockPos pos, int laenge, int breite, int hoehe)
	{
		int zahl = pos.getX();
		int zahl2 = pos.getY();
		int zahl3 = pos.getZ();
		
		for(int numba3 = hoehe; numba3 > 0; numba3--){
			for(int numba2 = laenge; numba2 > 0; numba2--){
			//rows
				for(int i = breite; i > 0; i--){
					if(world.getBlockState(pos) != block){
						return false;
					}
					pos.add(1, 0, 0);
				}
				pos.add(-zahl, 0, 1);	
			}
			pos.add(0, 1, -zahl3);
		}
		return true;
	}
	
	public static boolean checkBlockArea(World world, IBlockState[] block, BlockPos pos, int laenge, int breite, int hoehe)
	{	
		int zahl = pos.getX();
		int zahl2 = pos.getY();
		int zahl3 = pos.getZ();	
		
		for(int numba3 = hoehe; numba3 > 0; numba3--){
			for(int numba2 = laenge; numba2 > 0; numba2--){
			//rows
				for(int i = breite; i > 0; i--){
					for (IBlockState b : block) {
						if (world.getBlockState(pos) != b) {
							return false;
						} 
					}
					pos.add(1, 0, 0);
				}
				pos.add(-zahl, 0, 1);	
			}
			pos.add(0, 1, -zahl3);
		}
		return true;
	}
}
