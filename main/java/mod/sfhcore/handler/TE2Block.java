package mod.sfhcore.handler;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TE2Block {
	Block block;
	Class te;
	
	public TE2Block(Block b, Class te)
	{
		this.block = b;
		this.te = te;
	}
	
	public TE2Block(Class te, Block b)
	{
		this(b, te);
	}
}
