package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterTileEntity {
	
	public static List<TE2Block> tile_entitys = new ArrayList<TE2Block>();
	
	public static void add(Class te, Block b)
	{
		int i = getIndexForBlock(b);
		
		if(i == -1)
		{
			TE2Block te2block = new TE2Block(b, te);
			tile_entitys.add(te2block);
		}
	}
	
	public static void add(Block b, Class te)
	{
		add(te, b);
	}
	
	public static void register()
	{
		System.out.println("RegisterTileEntity");
		for(TE2Block t2b : tile_entitys)
		{
			System.out.println(t2b.te.getSuperclass());
			System.out.println(t2b.te);
			GameRegistry.registerTileEntity(t2b.te, t2b.block.getRegistryName());
		}
	}
	
	public static void registerTileEntity(Block b)
	{
		int i = getIndexForBlock(b);
			
		if(i != -1)
		{
			String domain = tile_entitys.get(i).block.getRegistryName().getResourceDomain();
			String tename = tile_entitys.get(i).te.getClass().toString().toLowerCase();
			GameRegistry.registerTileEntity(tile_entitys.get(i).te, new ResourceLocation(domain, tename));
		}
	}
	
	public static void registerTileEntity(TileEntity te)
	{
		int i = getIndexForTileEntity(te);
			
		if(i != -1)
		{
			String domain = tile_entitys.get(i).block.getRegistryName().getResourceDomain();
			String tename = tile_entitys.get(i).te.getClass().toString().toLowerCase();
			GameRegistry.registerTileEntity(te.getClass(), new ResourceLocation(domain, tename));
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
