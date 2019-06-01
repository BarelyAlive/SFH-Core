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
	
	public PlayerPosition(BlockPos pos)
	{
		this(pos, 0);
	}
	
	public PlayerPosition(BlockPos pos, float yaw)
	{
		this(pos, yaw, 0);
	}
	
	public PlayerPosition(BlockPos pos, float yaw, float ang)
	{
		this.pos = pos;
		this.yaw = yaw;
		this.ang = ang;
	}
	
	public BlockPos getPos()
	{
		if (this.pos == null)
			return new BlockPos(0, 0, 0);
		else
			return this.pos;
	}
	
	public void setPos(BlockPos pos)
	{
		this.pos = pos;
	}
	
	public void setPos(int x, int y, int z)
	{
		this.setPos(new BlockPos(x, y, z));
	}

	public float getYaw()
	{
		return this.yaw;
	}
	
	public void setYaw(float yaw)
	{
		this.yaw = yaw;
	}
	
	public float getAng()
	{
		return this.ang;
	}
	
	public void setAng(float ang)
	{
		this.ang = ang;
	}
	
}
