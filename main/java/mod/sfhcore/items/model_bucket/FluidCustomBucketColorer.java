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
        //if (tintIndex != 1) return 0xFFFFFFFF;
        if (!(stack.getItem() instanceof CustomBucket))
        {
        	return pixel;
        }
        CustomBucket bucket = (CustomBucket) stack.getItem();
        if (tintIndex == 0)
        {
        	return bucket.getColor();
        }
        else
        {
	        Block b = bucket.getContainedBlock();
	        if (b == Blocks.AIR)
	        {
	        	return pixel;
	        }
	        try {
	        	IBakedModel res = Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(new ItemStack(b));
		        return res.getParticleTexture().getFrameTextureData(0)[0][0];
			} catch (Exception e) {
	        	e.printStackTrace();
	        	return pixel;
			}
        }
	}
	
}
