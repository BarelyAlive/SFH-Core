package mod.sfhcore.helper;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class PlayerInventory
{
	public static boolean tryAddItem(final EntityPlayer player, final ItemStack stack)
	{
		if (player != null)
			if (!player.inventory.addItemStackToInventory(stack.copy()) && !player.world.isRemote) {
				final EntityItem item = new EntityItem(player.world, player.getPosition().getX(), player.getPosition().getY(),
						player.getPosition().getZ(), stack.copy());
				player.world.spawnEntity(item);
				return false;
			}
		return true;
	}
}
