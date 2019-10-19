package mod.sfhcore.network;

import mod.sfhcore.Constants;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MOD_ID);
	
	// Start the IDs at 1 so any unregistered messages (ID 0) throw a more obvious exception when received
	private static int messageID = 1;

	public static <REQ extends IMessage, REPLY extends IMessage> void registerMessage(final Class<? extends IMessageHandler<REQ, REPLY>> messageHandler, final Class<REQ> requestMessageType, final Side receivingSide) {
        NetworkHandler.INSTANCE.registerMessage(messageHandler, requestMessageType, messageID++, receivingSide);
    }

	public static void initPackets()
	{
		//CLIENT
		INSTANCE.registerMessage(MessageNBTUpdate.MessageNBTUpdateHandler.class, MessageNBTUpdate.class, messageID++, Side.CLIENT);
		INSTANCE.registerMessage(MessageCheckLight.MessageCheckLightHandler.class, MessageCheckLight.class, messageID++, Side.CLIENT);
	}

	public static void sendToAllAround(final IMessage message, final TileEntity te, final int range)
	{
		BlockPos pos = te.getPos();
		INSTANCE.sendToAllAround(message, new TargetPoint(te.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
	}

	public static void sendToAllAround(final IMessage message, final TileEntity te)
	{
		sendToAllAround(message, te, 64);
	}

	public static void sendToServer(final IMessage message)
	{
		INSTANCE.sendToServer(message);
	}

	public static void sendNBTUpdate(final TileEntity te)
	{
		sendToAllAround(new MessageNBTUpdate(te), te);
	}
}
