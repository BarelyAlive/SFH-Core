package mod.sfhcore.handler;

import net.minecraft.block.Block;
import net.minecraft.tileentity.TileEntity;

public class TE2Block {
	Block block;
	TileEntity te;
	
	public TE2Block(Block b, TileEntity te)
	{
		this.block = b;
		this.te = te;
	}
	
	public TE2Block(TileEntity te, Block b)
	{
		this(b, te);
	}
}
