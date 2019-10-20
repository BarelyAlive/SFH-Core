package mod.sfhcore.proxy;

import net.minecraft.block.Block;
import net.minecraftforge.fluids.Fluid;

public interface IProxy {

	default void initModel(final Fluid f, final Block b) {}
}
