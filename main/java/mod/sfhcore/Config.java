package mod.sfhcore;

import java.io.File;
import java.util.List;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	public static String[] oreDictPreferenceOrder = {"thermalfoundation", "immersiveengineering"};
	
	public static void loadConfigs()
	{
		Configuration config = new Configuration(new File(SFHCore.configDirectory, "NetherTweaksMod.cfg"));
		config.load();
		
        oreDictPreferenceOrder = config.getStringList("OreDict preference order", "Compat", oreDictPreferenceOrder, "Coffe has caffeine");
        
        config.save();
	}

}
