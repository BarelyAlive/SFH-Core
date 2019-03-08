package mod.sfhcore;
 
import mod.sfhcore.proxy.SFHCoreClientProxy;
import mod.sfhcore.proxy.SFHCoreProxy;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.world.WorldType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.terraingen.WorldTypeEvent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
 
@Mod(modid=Constants.ModIdSFHCORE, name=Constants.ModIdSFHCORE, version=Constants.SFHCoreVersion)
public class SFHCore {
     
    @Instance(value=Constants.ModIdSFHCORE)
    public static SFHCore instance;
    
    @SidedProxy(clientSide="mod.sfhcore.proxy.SFHCoreClientProxy", serverSide="mod.sfhcore.proxy.SFHCoreProxy")
    public static SFHCoreProxy proxy;
     
         
    @EventHandler
    public void PreInit(FMLPreInitializationEvent event){
        new SFHCoreClientProxy();
    }
    
    @EventHandler
    public void load(FMLInitializationEvent event){
        
    }
     
    @EventHandler
    public void PostInit(FMLPostInitializationEvent event){
    }
     
}