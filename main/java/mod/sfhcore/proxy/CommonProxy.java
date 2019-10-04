package mod.sfhcore.proxy;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;

public class CommonProxy {

	public void tryHandleBlockModel(final Block block, final ResourceLocation loc) {}

	public void tryHandleItemModel(final Item item, final ResourceLocation loc) {}

	public void tryHandleBlockModel(final ItemBlock block, final ResourceLocation loc) {}

	public void initModel(final Fluid f, final Block b) {}
}
