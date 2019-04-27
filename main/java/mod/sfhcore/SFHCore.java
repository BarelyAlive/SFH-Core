package mod.sfhcore;
 
import mod.sfhcore.handler.CustomFuelHandler;
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.proxy.SFHCoreProxy;
import mod.sfhcore.registries.RegisterBlocks;
import mod.sfhcore.registries.RegisterEnchantments;
import mod.sfhcore.registries.RegisterItems;
import mod.sfhcore.registries.RegisterTileEntity;
import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.world.WorldType;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
 
@Mod(modid=Constants.ModIdSFHCORE, name=Constants.ModIdSFHCORE, version=Constants.SFHCoreVersion)
public class SFHCore
{     
    @Instance(value=Constants.ModIdSFHCORE)
    public static SFHCore instance;
    
    @SidedProxy(clientSide="mod.sfhcore.proxy.SFHCoreClientProxy", serverSide="mod.sfhcore.proxy.SFHCoreProxy")
    public static SFHCoreProxy proxy;
     
    @Mod.EventBusSubscriber
    public static class RegistrationHandler
    {
    	@SubscribeEvent
    	public static void registerBlocks (RegistryEvent.Register<Block> event)
    	{
    		RegisterBlocks.registerBlocks(event.getRegistry());
    		RegisterTileEntity.register();
    	}
    	
    	@SubscribeEvent
    	public static void registerItems (RegistryEvent.Register<Item> event)
    	{
    		RegisterItems.register(event.getRegistry());
    		RegisterBlocks.registerItemBlocks(event.getRegistry());
    	}
    	
    	@SubscribeEvent
    	public static void registerEnchantments(RegistryEvent.Register<Enchantment> event)
    	{
    		RegisterEnchantments.registerEnchantments(event.getRegistry());
    	}
    	
    	@SubscribeEvent
    	public static void registerModels(ModelRegistryEvent event)
    	{
    		RegisterItems.registerModels();
    		RegisterBlocks.registerModels();
    	}
    }

    @Mod.EventHandler
    public void PreInit(FMLPreInitializationEvent event)
    {
        new SFHCoreClientProxy();
    }
    
    @Mod.EventHandler
    public void load(FMLInitializationEvent event)
    {
    	MinecraftForge.EVENT_BUS.register(new CustomFuelHandler());    
    }
    
    @Mod.EventHandler
    public void PostInit(FMLPostInitializationEvent event)
    {
    }
     
}