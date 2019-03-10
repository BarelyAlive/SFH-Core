package mod.sfhcore.handler;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@Mod.EventBusSubscriber
public class RegistrationHandler {
	
	@SubscribeEvent
	public static void registerBlocks (RegistryEvent.Register<Block> event)
	{
		RegisterBlocks.register(event.getRegistry());
	}
	
	@SubscribeEvent
	public static void registerItems (RegistryEvent.Register<Item> event)
	{
		RegisterItems.register(event.getRegistry());
		RegisterBlocks.registerItemBlocks(event.getRegistry());
	}
	
	public static void registerModels(ModelRegistryEvent event)
	{
		RegisterItems.registerModels();
		RegisterBlocks.registerModels();
	}

}
