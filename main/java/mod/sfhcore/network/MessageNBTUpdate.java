package mod.sfhcore.network;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class MessageNBTUpdate implements IMessage {

	public MessageNBTUpdate(){}

	private int x, y, z;
	private NBTTagCompound tag;
	public MessageNBTUpdate(final TileEntity te)
	{
		x = te.getPos().getX();
		y = te.getPos().getY();
		z = te.getPos().getZ();
		tag = te.writeToNBT(new NBTTagCompound());
	}

	@Override
	public void toBytes(final ByteBuf buf)
	{
		buf.writeInt(x);
		buf.writeInt(y);
		buf.writeInt(z);
		ByteBufUtils.writeTag(buf, tag);
	}

	@Override
	public void fromBytes(final ByteBuf buf)
	{
		x = buf.readInt();
		y = buf.readInt();
		z = buf.readInt();
		tag = ByteBufUtils.readTag(buf);
	}

	public static class MessageNBTUpdateHandler implements IMessageHandler<MessageNBTUpdate, IMessage> {
		@Override
		public IMessage onMessage(final MessageNBTUpdate msg, final MessageContext ctx)
		{
			net.minecraft.client.Minecraft.getMinecraft().addScheduledTask(() -> {
				TileEntity entity = net.minecraft.client.Minecraft.getMinecraft().world.getTileEntity(new BlockPos(msg.x, msg.y, msg.z));

				if (entity != null)
					entity.readFromNBT(msg.tag);
			});
			return null;
		}
	}

	/****
	 * Was code bevor.
	 * Used not client-side only code
	 ****/
	/*
	public static class MessageNBTUpdateHandler implements IMessageHandler<MessageNBTUpdate, IMessage> {
        @Override
        @SideOnly(Side.CLIENT)
        public IMessage onMessage(final MessageNBTUpdate msg, MessageContext ctx) {
            Minecraft.getMinecraft().addScheduledTask(new Runnable() {
                @Override
                @SideOnly(Side.CLIENT)
                public void run() {
                    TileEntity entity = Minecraft.getMinecraft().player.getEntityWorld().getTileEntity(new BlockPos(msg.x, msg.y, msg.z));

                    if (entity != null) {
                        entity.readFromNBT(msg.tag);
                    }
                }
            });
            return null;
        }
	}
	 */

}