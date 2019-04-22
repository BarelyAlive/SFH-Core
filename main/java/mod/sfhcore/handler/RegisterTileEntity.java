package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.List;

import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class RegisterTileEntity
{	
	private static List<TE2Block> tile_entitys = new ArrayList<TE2Block>();
	
	public static List<TE2Block> getTile_entitys()
	{
		return tile_entitys;
	}
	
	public static void add(Block b, Class te)
	{	
		try {
			b.getRegistryName();
		} catch (NullPointerException e) {
			LogUtil.fatal("SFHCore tried to register a tile entity, but the corresponding block was NULL!");
			return;
		}
		if(te == null)
		{
			LogUtil.fatal("SFHCore tried to register a tile entity, but it was NULL!");
			return;
		}
		if(b.getRegistryName() == null)
		{
			LogUtil.fatal("SFHCore tried to register a tile entity, but the corresponding block has a NULL name!");
			return;
		}
		
		int i = getIndexForBlock(b);
		
		if(i == -1)
		{
			TE2Block te2block = new TE2Block(b, te);
			tile_entitys.add(te2block);
		}
	}
	
	public static void register()
	{
		for(TE2Block t2b : tile_entitys)
		{
			GameRegistry.registerTileEntity(t2b.te, t2b.block.getRegistryName());
		}
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
