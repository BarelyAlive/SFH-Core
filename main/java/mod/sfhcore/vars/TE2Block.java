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

	public TE2Block(final Block b, final Class te)
	{
		block = b;
		this.te = te;
	}

	public TE2Block(final Class te, final Block b)
	{
		this(b, te);
	}
}
