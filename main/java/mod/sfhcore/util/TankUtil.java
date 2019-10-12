package mod.sfhcore.util;

import java.util.Objects;

import mod.sfhcore.blocks.tiles.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

public class TankUtil
{
	public static final ItemStack WATER_BOTTLE;

	static {
		NBTTagCompound waterPotion = new NBTTagCompound();
		waterPotion.setString("Potion", "minecraft:water");
		WATER_BOTTLE = new ItemStack(Items.POTIONITEM, 1, 0);
		WATER_BOTTLE.setTagCompound(waterPotion);
	}

	public static boolean drainWaterIntoBottle(final TileBase tileEntity, final EntityPlayer player, final FluidTank tank) {
		if (player.getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE)
			if (tank.getFluid() != null && tank.getFluidAmount() >= 250 && tank.getFluid().getFluid() == FluidRegistry.WATER) {
				if (!player.isCreative()) {
					player.addItemStackToInventory(WATER_BOTTLE.copy());
					player.getHeldItemMainhand().shrink(1);
				}
				tank.drain(250, true);

				tileEntity.markDirtyClient();
				return true;
			}
		return false;
	}

	public static boolean drainWaterFromBottle(final TileBase tileEntity, final EntityPlayer player, final FluidTank tank) {
		if (player.getHeldItemMainhand().getItem() == Items.POTIONITEM && Objects.equals(WATER_BOTTLE.getTagCompound(), player.getHeldItemMainhand().getTagCompound())) {
			FluidStack water = new FluidStack(FluidRegistry.WATER, 250);

			if (tank.fill(water, false) == water.amount)
				if (player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) {

					if (!player.isCreative())
						player.getHeldItemMainhand().shrink(1);
					tank.fill(water, true);

					tileEntity.markDirtyClient();
					return true;
				}
		}
		return false;
	}
}
