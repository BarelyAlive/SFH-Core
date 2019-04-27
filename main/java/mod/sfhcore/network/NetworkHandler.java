package mod.sfhcore.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import mod.sfhcore.Constants;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.NetworkRegistry.TargetPoint;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;

public class NetworkHandler {
	
	public static final SimpleNetworkWrapper INSTANCE = NetworkRegistry.INSTANCE.newSimpleChannel(Constants.ModIdSFHCORE);
	private static int id = 0;
	
	public static void registerMessage(Class a, Class b, Side side)
	{
		INSTANCE.registerMessage(a, b, id++, side);
	}
	
	public static void initPackets()
	{
		//CLIENT
		INSTANCE.registerMessage(MessageNBTUpdate.MessageNBTUpdateHandler.class, MessageNBTUpdate.class, id++, Side.CLIENT);
		INSTANCE.registerMessage(MessageCheckLight.MessageCheckLightHandler.class, MessageCheckLight.class, id++, Side.CLIENT);
	}
	
	public static void sendToAllAround(IMessage message, TileEntity te, int range) 
	{
		BlockPos pos = te.getPos();
        INSTANCE.sendToAllAround(message, new TargetPoint(te.getWorld().provider.getDimension(), pos.getX(), pos.getY(), pos.getZ(), range));
    }
	
	public static void sendToAllAround(IMessage message, TileEntity te) 
	{
        sendToAllAround(message, te, 64);
    }
	
	public static void sendNBTUpdate(TileEntity te) {
		sendToAllAround(new MessageNBTUpdate(te), te);
	}
}
