package mod.sfhcore.handler;

import mod.sfhcore.blocks.Milk;
import mod.sfhcore.fluid.FluidMilk;
import net.minecraft.block.Block;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ModFluids
{
	public static final FluidMilk FLUID_MILK = new FluidMilk();
	public static final Milk BLOCK_MILK = new Milk();
	
	@SideOnly(Side.CLIENT)
    public static void initModels() {
        FLUID_MILK.initModel();
    }
	
	@SubscribeEvent
	public void registerBlocks(RegistryEvent.Register<Block> e)
	{
		e.getRegistry().register(BLOCK_MILK);
	}
	
	@SubscribeEvent
	public void registerModels(ModelRegistryEvent e)
	{
		initModels();
	}
}
