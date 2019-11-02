package mod.sfhcore.world;

import mod.sfhcore.Constants;
import mod.sfhcore.handler.BucketHandler;
import mod.sfhcore.handler.ModFluids;
import mod.sfhcore.helper.NotNull;
import mod.sfhcore.helper.PlayerInventory;
import mod.sfhcore.items.CustomBucket;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EventHook
{
	//Enitity Interaction
	@SubscribeEvent
	public void getMilk(final PlayerInteractEvent.EntityInteract event)
	{
		final ItemStack stack = event.getItemStack();
		final EntityPlayer player = event.getEntityPlayer();
				
		if(event.getTarget() instanceof EntityCow && event.getTarget().getName().equals("Cow"))
		{
			final EntityCow cow = (EntityCow) event.getTarget();
			
			if (!cow.isChild() && NotNull.checkNotNull(stack))
			{
				final Item item = stack.getItem();
				String material = "";
				
				if(item instanceof CustomBucket)
				{
					material = ((CustomBucket) item).getMaterial();
					
					if (!material.isEmpty() && item == BucketHandler.getBucketFromFluid(null, material)) {
						stack.shrink(1);
						PlayerInventory.tryAddItem(player, new ItemStack(BucketHandler.getBucketFromFluid(FluidRegistry.getFluid(Constants.MILK), material)));
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void pourMilk(final PlayerInteractEvent.RightClickBlock event)
	{
		BlockPos pos = event.getPos();
		final World world = event.getWorld();
		final IBlockState state = world.getBlockState(pos);
		final boolean vaporize = world.provider.doesWaterVaporize();
		final EntityPlayer player = event.getEntityPlayer();
		final ItemStack stack = event.getItemStack();

		if (player == null
				|| stack.getItem() != Items.MILK_BUCKET) return;

		if (state.getBlock().onBlockActivated(world, pos, state, player, event.getHand(), event.getFace(), (float)event.getHitVec().x, (float)event.getHitVec().y, (float)event.getHitVec().z))
			event.setCanceled(true);
		else
		{
			pos.add(0.5D, 0.5D, 0.5D);

			switch (event.getFace())
			{
			case UP:
				pos = pos.up();
				break;
			case NORTH:
				pos = pos.north();
				break;
			case EAST:
				pos = pos.east();
				break;
			case SOUTH:
				pos = pos.south();
				break;
			case WEST:
				pos = pos.west();
				break;
			case DOWN:
				pos = pos.down();
				break;
			}
			
			stack.shrink(1);
			player.addItemStackToInventory(new ItemStack(Items.BUCKET));
			
			if(vaporize)
				ModFluids.FLUID_MILK.vaporize(player, world, pos, new FluidStack(ModFluids.FLUID_MILK, 1000));
			else
				world.setBlockState(pos, ModFluids.BLOCK_MILK.getDefaultState());
		}
	}
}
