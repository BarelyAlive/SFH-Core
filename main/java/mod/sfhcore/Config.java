package mod.sfhcore;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

public class Config {
	
	//int
	public static int nethDim;
	public static int endDim;

	public static void loadConfigs(FMLPreInitializationEvent event){
		Configuration Config = new Configuration(event.getSuggestedConfigurationFile());
        Config.load();
	    //Dimension Travel
	    nethDim = 			Config.get("World", "To which dimension shall the nether portal send you back?", 0).getInt();
	    endDim = 			Config.get("World", "To which Dimension shall an end portal send you back?", 0).getInt();
	    
	    Config.save();
	}
}
