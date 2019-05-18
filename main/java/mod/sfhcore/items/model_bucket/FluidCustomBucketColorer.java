package mod.sfhcore.items.model_bucket;

import mod.sfhcore.items.*;
import net.minecraft.block.*;
import net.minecraft.client.*;
import net.minecraft.client.renderer.block.model.*;
import net.minecraft.client.renderer.color.*;
import net.minecraft.init.*;
import net.minecraft.item.*;
import net.minecraftforge.fluids.*;

public class FluidCustomBucketColorer implements IItemColor
{

	@Override
	public int colorMultiplier(ItemStack stack, int tintIndex) {
		int pixel = 0xFFFFFFFF;
        if (!(stack.getItem() instanceof CustomBucket))
        {
        	return pixel;
        }
        CustomBucket bucket = (CustomBucket) stack.getItem();
        if (tintIndex == 2)
        {
        	return bucket.getColor();
        }
        else if (tintIndex == 0)
        {
	        Block b = bucket.getContainedBlock();
	        if (b == Blocks.AIR)
	        {
	        	
	        	return bucket.getColor();
	        }
	        try {
	        	// First get Texture and then 1 Pixel from the Fluid
	        	IBakedModel res = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(b));
		        return ((res.getParticleTexture().getFrameTextureData(0)[0][0] & 0xFFFFFF) | 0x00000000);
			} catch (Exception e) {
	        	e.printStackTrace();
	        	return pixel;
			}
        }
        return 0xFF000000;
	}
	
}
