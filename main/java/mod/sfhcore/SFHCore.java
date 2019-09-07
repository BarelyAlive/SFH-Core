package mod.sfhcore;

import java.io.File;

import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.handler.CustomFuelHandler;
import mod.sfhcore.network.NetworkHandler;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.proxy.SFHCoreProxy;
import mod.sfhcore.registries.RegisterBlocks;
import mod.sfhcore.registries.RegisterEnchantments;
import mod.sfhcore.registries.RegisterItems;
import mod.sfhcore.registries.RegisterTileEntity;
import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
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

@Mod(modid=Constants.ModIdSFHCORE, name=Constants.ModIdSFHCORE, version=Constants.SFHCoreVersion)
public class SFHCore
{
	@Instance(value=Constants.ModIdSFHCORE)
	public static SFHCore instance;

	@SidedProxy(clientSide="mod.sfhcore.proxy.SFHCoreClientProxy", serverSide="mod.sfhcore.proxy.SFHCoreProxy")
	public static SFHCoreProxy proxy;

	public static File configDirectory;

	static
	{
		FluidRegistry.enableUniversalBucket();
	}

	@Mod.EventBusSubscriber
	public static class RegistrationHandler
	{
		@SubscribeEvent
		public static void registerBlocks (final RegistryEvent.Register<Block> event)
		{
			RegisterBlocks.registerBlocks(event.getRegistry());
			RegisterTileEntity.register();
		}

		@SubscribeEvent
		public static void registerItems (final RegistryEvent.Register<Item> event)
		{
			RegisterItems.register(event.getRegistry());
			RegisterBlocks.registerItemBlocks(event.getRegistry());
		}

		@SubscribeEvent
		public static void registerEnchantments(final RegistryEvent.Register<Enchantment> event)
		{
			RegisterEnchantments.registerEnchantments(event.getRegistry());
		}

		@SubscribeEvent
		@SideOnly(Side.CLIENT)
		public static void registerModels(final ModelRegistryEvent event)
		{
			RegisterItems.registerModels();
			RegisterBlocks.registerModels();
		}
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
		configDirectory = new File(event.getModConfigurationDirectory(), Constants.ModIdSFHCORE);
		configDirectory.mkdirs();

		LogUtil.setup(Constants.ModIdSFHCORE, configDirectory);

		Config.loadConfigs();

		NetworkHandler.initPackets();
		new SFHCoreClientProxy();
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