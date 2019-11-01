package mod.sfhcore.items.model_bucket;

import mod.sfhcore.items.CustomBucket;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.color.IItemColor;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

public class FluidCustomBucketColorer implements IItemColor
{

	@Override
	public int colorMultiplier(final ItemStack stack, final int tintIndex) {
		int pixel = 0xFFFFFFFF;
		if (!(stack.getItem() instanceof CustomBucket))
			return pixel;
		CustomBucket bucket = (CustomBucket) stack.getItem();
		if (tintIndex == 2)
			return bucket.getColor();
		else
		{
			Block b = bucket.getContainedBlock();
			if (b == Blocks.AIR)
				return bucket.getColor();
			try {
				// First get Texture and then 1 Pixel from the Fluid
				if (b == Blocks.LAVA || b == Blocks.FLOWING_LAVA)
					return 0xFFDC711D;
				else if (b == Blocks.WATER || b == Blocks.FLOWING_WATER)
					return 0xFF354FF4;
				else
				{
					IBakedModel res = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(b));
					for(int i = 0; i < res.getParticleTexture().getFrameCount(); i++)
						if(res.getParticleTexture().getFrameTextureData(0)[i].length == 256)
							return res.getParticleTexture().getFrameTextureData(0)[i][0] & 0xFFFFFF | 0xFF000000;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return pixel;
			}
		}
		return 0xFF000000;
	}

}
