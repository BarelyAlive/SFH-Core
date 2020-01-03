package mod.sfhcore.proxy;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

public interface IProxy {
	
	default void preInit() {}
	default void init() {}
	default void postInit() {}

	default void initModel(final Fluid f, final Block b) {}
}
