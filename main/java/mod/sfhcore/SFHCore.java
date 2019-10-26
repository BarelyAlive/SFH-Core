package mod.sfhcore;

import java.io.File;

import mod.sfhcore.handler.BucketRegistrationHandler;
import mod.sfhcore.handler.ModFuelHandler;
import mod.sfhcore.network.NetworkHandler;
import mod.sfhcore.proxy.IProxy;
import mod.sfhcore.util.LogUtil;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

@Mod(modid=Constants.MOD_ID, name=Constants.MOD_ID, version=Constants.VERSION, acceptedMinecraftVersions=Constants.MC_VERSION)
public class SFHCore
{
	public static File configDirectory;
	
	@Instance(value=Constants.MOD_ID)
	private static SFHCore instance;

	public static SFHCore getInstance() {
		return instance;
	}
	
	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@SidedProxy(clientSide=Constants.CLIENT_PROXY, serverSide=Constants.SERVER_PROXY)
	public static IProxy proxy;

	@Mod.EventHandler
	public void PreInit(final FMLPreInitializationEvent event)
	{
		configDirectory = new File(event.getModConfigurationDirectory(), Constants.MOD_ID);
		configDirectory.mkdirs();

		LogUtil.setup(Constants.MOD_ID, configDirectory);

		Config.loadConfigs();

		NetworkHandler.initPackets();
		
		MinecraftForge.EVENT_BUS.register(new BucketRegistrationHandler());
	}

	@Mod.EventHandler
	public void load(final FMLInitializationEvent event)
	{
		MinecraftForge.EVENT_BUS.register(new ModFuelHandler());
	}

	@Mod.EventHandler
	public void PostInit(final FMLPostInitializationEvent event) {}
}