package mod.sfhcore.handler;

import net.minecraft.item.Item;
import net.minecraftforge.client.event.ColorHandlerEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BucketRegistrationHandler
{
	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void registerBuckets(final RegistryEvent.Register<Item> event)
	{
		BucketHandler.registerBuckets(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public void registerItemHandlers(final ColorHandlerEvent.Item event)
	{
		BucketHandler.registerItemHandlers(event);
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	@SideOnly(Side.CLIENT)
	public void registerBucketModels(final ModelRegistryEvent event)
	{
		BucketHandler.registerBucketModels(event);
	}
}
