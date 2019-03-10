package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterTileEntity {
	
	public static List<TE2Block> tile_entitys = new ArrayList<TE2Block>();
	
	public static void add(TileEntity te, Block b)
	{
		int i = getIndexForBlock(b);
		
		if(i == -1)
		{
			TE2Block te2block = new TE2Block(b, te);
		}
	}
	
	public static void add(Block b, TileEntity te)
	{
		add(te, b);
	}
	
	public static void registerTileEntity(Block b)
	{
		int i = getIndexForBlock(b);
			
		if(i != -1)
		{
			GameRegistry.registerTileEntity(tile_entitys.get(i).te.getClass(), b.getRegistryName());
		}
	}
	
	public static void registerTileEntity(TileEntity te)
	{
		int i = getIndexForTileEntity(te);
			
		if(i != -1)
		{
			GameRegistry.registerTileEntity(te.getClass(), tile_entitys.get(i).block.getRegistryName());
		}
	}
	
	private static int getIndexForTileEntity(TileEntity te)
	{
		for(int i = 0; i < tile_entitys.size(); i++)
		{
			if(tile_entitys.get(i).te.equals(te))
			{
				return i;
			}
		}
		return -1;
	}
	
	private static int getIndexForBlock(Block b)
	{
		for(int i = 0; i < tile_entitys.size(); i++)
		{
			if(tile_entitys.get(i).block.equals(b))
			{
				return i;
			}
		}
		return -1;
	}
	
}
