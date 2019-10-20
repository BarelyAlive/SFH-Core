package mod.sfhcore;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

	public static boolean useAllLavaContainer = true;

	public static void loadConfigs()
	{
		Configuration config = new Configuration(new File(SFHCore.configDirectory, "NetherTweaksMod.cfg"));
		config.load();

		useAllLavaContainer = config.getBoolean("Can every lava container be used as fuel in furnace?", "Tweak", true, "");

		config.save();
	}

}
