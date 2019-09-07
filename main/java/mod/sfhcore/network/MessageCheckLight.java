package mod.sfhcore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

// When all else fails, do it yourself
// (I couldn't find any way to force a lighting update on the client without some blockstate hackery)
// (I thought packet hackery would be better)
public class MessageCheckLight implements IMessage
{
	private int x, y, z;

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public MessageCheckLight()
	{

	}

	public MessageCheckLight(final BlockPos pos)
	{
		x = pos.getX();
		y = pos.getY();
		z = pos.getZ();
	}

	@Override
	public void fromBytes(final ByteBuf buffer)
	{
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
	}

	@Override
	public void toBytes(final ByteBuf buffer)
	{
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
	}

	public static class MessageCheckLightHandler implements IMessageHandler<MessageCheckLight, IMessage>
	{
		@Override
		public IMessage onMessage(final MessageCheckLight message, final MessageContext ctx)
		{
			BlockPos pos = new BlockPos(message.getX(), message.getY(), message.getZ());

			net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> net.minecraft.client.Minecraft.getMinecraft().world.checkLight(pos));

			return null;
		}
	}
}