package mod.sfhcore.fluid;

import mod.sfhcore.Constants;
import mod.sfhcore.helper.FluidStateMapper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FluidMilk extends Fluid
{
	public static final ResourceLocation STILL = new ResourceLocation("sfhcore:blocks/milk_still");
	public static final ResourceLocation FLOW = new ResourceLocation("sfhcore:blocks/milk_flow");

	public FluidMilk() {
		super(Constants.MILK, STILL, FLOW);
		setDensity(FluidRegistry.WATER.getDensity());
		setLuminosity(FluidRegistry.WATER.getLuminosity());
		setTemperature(FluidRegistry.WATER.getTemperature());
		setViscosity(FluidRegistry.WATER.getViscosity());
		
		FluidRegistry.registerFluid(this);
	}
	
	@Override
	public SoundEvent getEmptySound(final FluidStack stack) {
		return SoundEvents.ITEM_BUCKET_FILL;
	}

	@Override
	public boolean doesVaporize(final FluidStack fluidStack) {
		return fluidStack.getFluid() == this;
	}

	@SideOnly(Side.CLIENT)
    public void initModel() {
        final Block block = getBlock();

        final FluidStateMapper mapper = new FluidStateMapper(Constants.MOD_ID, this);

        final Item item = Item.getItemFromBlock(block);
        if (item != Items.AIR) {
            ModelBakery.registerItemVariants(item);
            ModelLoader.setCustomMeshDefinition(item, mapper);
        }

        ModelLoader.setCustomStateMapper(block, mapper);
    }
}
