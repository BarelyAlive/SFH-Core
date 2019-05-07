package mod.sfhcore.util;

import mod.sfhcore.blocks.tiles.TileBase;
import mod.sfhcore.blocks.tiles.TileFluidInventory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class TankUtil {
    private static final ItemStack WATER_BOTTLE;

    static {
        NBTTagCompound waterPotion = new NBTTagCompound();
        waterPotion.setString("Potion", "minecraft:water");
        WATER_BOTTLE = new ItemStack(Items.POTIONITEM, 1, 0);
        WATER_BOTTLE.setTagCompound(waterPotion);
    }
    
    public static boolean fillFromHand(EntityPlayer player, EnumHand hand, TileFluidInventory te)
	{
		ItemStack held = player.getHeldItem(hand);
		if(held.isEmpty()) return false;
		FluidStack f = FluidUtil.getFluidContained(held);
		if(f == null) return false;
		IFluidHandler heldFH = FluidUtil.getFluidHandler(held);
		if(heldFH == null) return false;
		
		//IF WATER_BOTTLE
		if (player.getHeldItemMainhand().getItem() == Items.POTIONITEM && WATER_BOTTLE.getTagCompound().equals(player.getHeldItemMainhand().getTagCompound())) {
            FluidStack water = new FluidStack(FluidRegistry.WATER, 250);

            if (te.fill(water, false) == water.amount) {
                if (player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) {

                    if (!player.isCreative())
                        player.getHeldItemMainhand().shrink(1);
                    
                    te.markDirtyClient();
                    return true;
                	}
                }
			}
		
		if(FluidUtil.tryFluidTransfer(te, heldFH, f, true) == null) return false;
		return true;
	}
    
    public static boolean fillToHand(EntityPlayer player, EnumHand hand, TileFluidInventory te)
	{
		ItemStack held = player.getHeldItem(hand);
		if(held.isEmpty()) return false;
		FluidStack f = FluidUtil.getFluidContained(held);
		if(f == null) return false;
		IFluidHandler heldFH = FluidUtil.getFluidHandler(held);
		if(heldFH == null) return false;
		
		//IF WATER_BOTTLE
		if (player.getHeldItemMainhand().getItem() == Items.POTIONITEM && WATER_BOTTLE.getTagCompound().equals(player.getHeldItemMainhand().getTagCompound())) {
            FluidStack water = new FluidStack(FluidRegistry.WATER, 250);

            if (te.fill(water, false) == water.amount) {
                if (player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) {

                    if (!player.isCreative())
                        player.getHeldItemMainhand().shrink(1);
                    te.fill(water, true);

                    te.markDirtyClient();
                    return true;
                }
            }
        }
		
		if(FluidUtil.tryFluidTransfer(te, heldFH, f, true) == null) return false;
		return true;
	}

    public static boolean drainWaterIntoBottle(TileBase tileEntity, EntityPlayer player, FluidTank tank) {
        if (player.getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE) {
            if (tank.getFluid() != null && tank.getFluidAmount() >= 250 && tank.getFluid().getFluid() == FluidRegistry.WATER) {
                if (player.addItemStackToInventory(WATER_BOTTLE.copy())) {
                    if (!player.isCreative())
                        player.getHeldItemMainhand().shrink(1);
                    tank.drain(250, true);

                    tileEntity.markDirtyClient();
                    return true;
                }
            }
        }

        return false;
    }

    public static boolean drainWaterFromBottle(TileBase tileEntity, EntityPlayer player, FluidTank tank) {
        if (player.getHeldItemMainhand().getItem() == Items.POTIONITEM && WATER_BOTTLE.getTagCompound().equals(player.getHeldItemMainhand().getTagCompound())) {
            FluidStack water = new FluidStack(FluidRegistry.WATER, 250);

            if (tank.fill(water, false) == water.amount) {
                if (player.addItemStackToInventory(new ItemStack(Items.GLASS_BOTTLE))) {

                    if (!player.isCreative())
                        player.getHeldItemMainhand().shrink(1);
                    tank.fill(water, true);

                    tileEntity.markDirtyClient();
                    return true;
                }
            }
        }

        return false;
    }

}
