package mod.sfhcore.fluid;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.FluidRegistry;

public class Fluid extends net.minecraftforge.fluids.Fluid {

	public Fluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
		FluidRegistry.registerFluid(this);
	}
	
}
