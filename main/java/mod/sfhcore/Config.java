package mod.sfhcore;

import java.io.File;

import net.minecraftforge.common.config.Configuration;

public class Config {

	public static boolean useAllHotFluidContainer = true;

	public static void loadConfigs()
	{
		final Configuration config = new Configuration(new File(SFHCore.configDirectory, "NetherTweaksMod.cfg"));
		config.load();

		useAllHotFluidContainer = config.getBoolean("Can every bucket containing a hot fluid (hotter than lava) be used as fuel in furnace?", "Tweak", true, "");

		config.save();
	}

}
