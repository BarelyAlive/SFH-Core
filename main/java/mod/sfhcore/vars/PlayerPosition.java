package mod.sfhcore.vars;

import net.minecraft.util.math.BlockPos;

public class PlayerPosition {

	private BlockPos pos;
	private float yaw;
	private float ang;

	public PlayerPosition()
	{
		this(null);
	}

	public PlayerPosition(final BlockPos pos)
	{
		this(pos, 0);
	}

	public PlayerPosition(final BlockPos pos, final float yaw)
	{
		this(pos, yaw, 0);
	}

	public PlayerPosition(final BlockPos pos, final float yaw, final float ang)
	{
		this.pos = pos;
		this.yaw = yaw;
		this.ang = ang;
	}

	public BlockPos getPos()
	{
		if (pos == null)
			return new BlockPos(0, 0, 0);
		else
			return pos;
	}

	public void setPos(final BlockPos pos)
	{
		this.pos = pos;
	}

	public void setPos(final int x, final int y, final int z)
	{
		this.setPos(new BlockPos(x, y, z));
	}

	public float getYaw()
	{
		return yaw;
	}

	public void setYaw(final float yaw)
	{
		this.yaw = yaw;
	}

	public float getAng()
	{
		return ang;
	}

	public void setAng(final float ang)
	{
		this.ang = ang;
	}

}
