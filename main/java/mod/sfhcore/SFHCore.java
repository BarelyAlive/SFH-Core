package mod.sfhcore;

import java.io.File;

import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.handler.CustomFuelHandler;
import mod.sfhcore.network.NetworkHandler;
import mod.sfhcore.proxy.ClientProxy;
import mod.sfhcore.proxy.CommonProxy;
import mod.sfhcore.util.LogUtil;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@Mod(modid=Constants.MODID, name=Constants.MODID, version=Constants.VERSION)
public class SFHCore
{
	@Instance(value=Constants.MODID)
	public static SFHCore instance;

	@SidedProxy(clientSide=Constants.CLIENT_PROXY, serverSide=Constants.SERVER_PROXY)
	private static CommonProxy proxy;
	
	public static CommonProxy getProxy() {
		return proxy;
	}

	@SideOnly(Side.CLIENT)
	public static ClientProxy getClientProxy() {
		return (ClientProxy) proxy;
	}

	public static File configDirectory;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@Mod.EventBusSubscriber
	public static class BucketRegistrationHandler
	{
		@SubscribeEvent(priority = EventPriority.LOWEST)
		public static void registerBuckets (final RegistryEvent.Register<Item> event)
		{
			BucketHandler.registerBuckets(event);
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		@SideOnly(Side.CLIENT)
		public static void registerItemHandlers(final ColorHandlerEvent.Item event)
		{
			BucketHandler.registerItemHandlers(event);
		}

		@SubscribeEvent(priority = EventPriority.LOWEST)
		@SideOnly(Side.CLIENT)
		public static void registerBucketModels(final ModelRegistryEvent event)
		{
			BucketHandler.registerBucketModels(event);
		}
	}

	@Mod.EventHandler
	public void PreInit(final FMLPreInitializationEvent event)
	{
		configDirectory = new File(event.getModConfigurationDirectory(), Constants.MODID);
		configDirectory.mkdirs();

		LogUtil.setup(Constants.MODID, configDirectory);

		Config.loadConfigs();

		NetworkHandler.initPackets();
		new ClientProxy();
	}

	@Mod.EventHandler
	public void load(final FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new CustomFuelHandler());
	}

	@Mod.EventHandler
	public void PostInit(final FMLPostInitializationEvent event)
	{
	}

}