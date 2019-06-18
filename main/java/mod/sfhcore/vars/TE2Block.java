package mod.sfhcore.vars;

import net.minecraft.block.Block;

public class TE2Block {
	private Block block;
	private Class te;
	
	public Block getBlock() {
		return block;
	}

	public Class getTe() {
		return te;
	}

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
