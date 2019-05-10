package mod.sfhcore.util;

import mod.sfhcore.blocks.tiles.TileBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumHand;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;
import net.minecraftforge.fluids.FluidUtil;
import net.minecraftforge.fluids.capability.IFluidHandlerItem;

public class TankUtil {
    private static final ItemStack WATER_BOTTLE;

    static {
        NBTTagCompound waterPotion = new NBTTagCompound();
        waterPotion.setString("Potion", "minecraft:water");
        WATER_BOTTLE = new ItemStack(Items.POTIONITEM, 1, 0);
        WATER_BOTTLE.setTagCompound(waterPotion);
    }
    
    public static boolean fillTank(TileBase te, EntityPlayer player, EnumHand hand, FluidTank tank)
    {
    	if(drainWaterFromBottle(te, player, tank)) return true;
    	if(fillfromBucket(te, player, hand, tank)) return true;
    	
    	IFluidHandlerItem ifhi = FluidUtil.getFluidHandler(player.getHeldItem(hand));
    	if(ifhi == null) return false;
    	FluidStack water = new FluidStack(FluidRegistry.WATER, Integer.MAX_VALUE);
    	
    	if(FluidUtil.tryFluidTransfer(tank, ifhi, water, true) != null)
		{
			te.markDirtyClient();
			return true;
		}
    	
    	
    	return false;
    }
    
    public static boolean fillfromBucket(TileBase te, EntityPlayer player, EnumHand hand, FluidTank tank)
    {
    	if (player.getHeldItemMainhand().getItem() == Items.WATER_BUCKET) {
    		FluidStack water = new FluidStack(FluidRegistry.WATER, 1000);
    		if (tank.fill(water, false) == water.amount) {
                if (!player.isCreative()) {

                    player.addItemStackToInventory(new ItemStack(Items.BUCKET));
                    player.getHeldItemMainhand().shrink(1);
                }
                    tank.fill(water, true);

                    te.markDirtyClient();
                    return true;
            }
        }

        return false;
    }

    public static boolean drainWaterIntoBottle(TileBase tileEntity, EntityPlayer player, FluidTank tank) {
        if (player.getHeldItemMainhand().getItem() == Items.GLASS_BOTTLE) {
            if (tank.getFluid() != null && tank.getFluidAmount() >= 250 && tank.getFluid().getFluid() == FluidRegistry.WATER) {
                if (!player.isCreative()) {
                	player.addItemStackToInventory(WATER_BOTTLE.copy());
                    player.getHeldItemMainhand().shrink(1);
                }
                    tank.drain(250, true);

                    tileEntity.markDirtyClient();
                    return true;
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
