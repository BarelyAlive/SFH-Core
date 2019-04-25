package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterTileEntity
{	
	private static NonNullList<TE2Block> tile_entitys = NonNullList.create();
	
	public static List<TE2Block> getTile_entitys()
	{
		return tile_entitys;
	}
	
	public static void register()
	{
		for(TE2Block t2b : tile_entitys)
		{
			GameRegistry.registerTileEntity(t2b.getTe(), t2b.getBlock().getRegistryName());
		}
	}
	
	public static int getIndexForBlock(Block b)
	{
		for(int i = 0; i < tile_entitys.size(); i++)
		{
			if(tile_entitys.get(i).getBlock().equals(b))
			{
				return i;
			}
		}
		return -1;
	}
}
