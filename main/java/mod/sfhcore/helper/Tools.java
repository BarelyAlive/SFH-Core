package mod.sfhcore.helper;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
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
	 * @return true if area contains that block
	 */
	public static boolean checkBlockArea(World world, Block block, BlockPos pos, int laenge, int breite, int hoehe){		
		
		for(int actualY = hoehe; actualY > 0; actualY--){
			for(int actualZ = laenge; actualZ > 0; actualZ--){
				for(int actualX = breite; actualX > 0; actualX--){
					if(world.getBlockState(pos.add(actualX, actualY, actualZ)).getBlock() != block){
						return false;
					}
				}
			}
		}
		return true;
	}
	
	/**
	 * Sets an area to one block.
	 * Starts in the left corner on the lowest level and counts from left to right, up to down and bottom to top.
	 * 
	 * @param world
	 * @param block
	 * @param pos
	 * @param laenge
	 * @param breite
	 * @param hoehe
	 */
	public static void setBlockArea(World world, Block block, BlockPos pos, int laenge, int breite, int hoehe){
		
		for(int actualY = hoehe; actualY > 0; actualY--){
			for(int actualZ = laenge; actualZ > 0; actualZ--){
				for(int actualX = breite; actualX > 0; actualX--){
					world.setBlockState(pos.add(actualX, actualY, actualZ), block.getDefaultState());
				}
			}
		}
	}
	
	/** Check if the players hand is empty
	 * 
	 * @param player
	 * @return true if hand is empty
	 */
	public static boolean checkHandEmpty(EntityPlayer player){
    	if(player.inventory.getCurrentItem() == null){
    		return true;
    	}
    	else{
    		return false;
    	}
    }
}
