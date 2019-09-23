package mod.sfhcore.network;

import mod.sfhcore.Constants;
import mod.sfhcore.util.LogUtil;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler
{
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.MODID);
	private static int id = 0;

	public static void registerMessage(final Class messageHandler, final Class message, final Side side)
	{
		try {
			INSTANCE.registerMessage(messageHandler, message, id++, side);
		} catch (IllegalArgumentException e) {
			LogUtil.error("SFHCore tried to register a message with illegal arguments!");
			e.printStackTrace();
		}
	}

	public static void initPackets()
	{
		//CLIENT
		INSTANCE.registerMessage(MessageNBTUpdate.MessageNBTUpdateHandler.class, MessageNBTUpdate.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageCheckLight.MessageCheckLightHandler.class, MessageCheckLight.class, id++, Side.CLIENT);
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
