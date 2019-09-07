package mod.sfhcore.modules;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

public interface ISFHCoreModule {

	String getMODID();

	default void registerBlocks(final IForgeRegistry<Block> registry){}
	default void registerItems(final IForgeRegistry<Item> registry){}
	default void registerOredicts(){}

	// Called inside CommonProxy
	default void preInit(final FMLPreInitializationEvent event){}
	default void init(final FMLInitializationEvent event){}
	default void postInit(final FMLPostInitializationEvent event){}

	// Called inside ClientProxy
	@SideOnly(Side.CLIENT)
	default void preInitClient(final FMLPreInitializationEvent event){}
	@SideOnly(Side.CLIENT)
	default void initClient(final FMLInitializationEvent event){}
	@SideOnly(Side.CLIENT)
	default void postInitClient(final FMLPostInitializationEvent event){}

	@SideOnly(Side.CLIENT)
	default void initBlockModels(final ModelRegistryEvent e){}
	@SideOnly(Side.CLIENT)
	default void initItemModels(final ModelRegistryEvent e){}

	// Called inside ServerProxy ... unlikely to be used
	@SideOnly(Side.SERVER)
	default void preInitServer(final FMLPreInitializationEvent event){}
	@SideOnly(Side.SERVER)
	default void initServer(final FMLInitializationEvent event){}
	@SideOnly(Side.SERVER)
	default void postInitServer(final FMLPostInitializationEvent event){}


}
