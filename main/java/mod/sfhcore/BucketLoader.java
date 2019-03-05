package mod.sfhcore;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBucket;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import mod.nethertweaks.blocks.BlockDemonWater;
import mod.nethertweaks.blocks.NTMBlocks;
import mod.nethertweaks.handler.BucketHandler;
import mod.nethertweaks.items.BucketDemonWater;
import mod.nethertweaks.items.NTMItems;
import mod.sfhcore.Registry;


public class BucketLoader {

		//Material
		public static Material demonWater;
		
		//Buckets
		public static Item bucketDemonWater;
		public static Item itemBucketNTM;
		public static Item itemBucketNTMWater;
		public static Item itemBucketNTMDemonWater;
		public static Item itemBucketNTMLava;
		public static Item bucketWood;
		public static Item bucketWoodWater;
		public static Item bucketWoodDemonWater;
		public static Item bucketStone;
		public static Item bucketStoneWater;
		public static Item bucketStoneLava;
		public static Item bucketStoneDemonWater;
		
	
	public void registerBuckets(){
		
		//Buckets
		
		//Demon Water if necessary
		
		if(Config.iwantvanillaWater == false){
		bucketDemonWater = new BucketDemonWater(blockDemonWater);
		bucketDemonWater.setUnlocalizedName(INames.BUCKETDEMONWATER).setContainerItem(Items.BUCKET);
		
		bucketDemonWater.setRegistryName(INames.BUCKETDEMONWATER);
		bucketDemonWater.registerItems();
		}
		
		if(Config.iwantvanillaWater == false){
			BucketHandler.INSTANCE.buckets.put(blockDemonWater, bucketDemonWater);
		}
		if(Config.iwantvanillaWater == true){
			BucketHandler.INSTANCE.buckets.put(blockDemonWater, itemBucketNTMDemonWater);
			BucketHandler.INSTANCE.buckets.put(Blocks.FLOWING_WATER, itemBucketNTMWater);
			BucketHandler.INSTANCE.buckets.put(Blocks.FLOWING_LAVA, itemBucketNTMLava);
			BucketHandler.INSTANCE.buckets.put(Blocks.AIR, BucketLoader.itemBucketNTM);
		}
		
		//Regular Buckets
		
		bucketStoneWater = new BucketStone(Blocks.FLOWING_WATER, INames.BUCKETSTONEWATER);
		bucketStoneWater.setUnlocalizedName(INames.BUCKETSTONEWATER).setContainerItem(BucketLoader.bucketStone);
		
		bucketStoneWater.setRegistryName(INames.BUCKETSTONEWATER);
		bucketStoneWater.registerItems();
		
		bucketStoneLava = new BucketStone(Blocks.FLOWING_LAVA, INames.BUCKETSTONELAVA);
		bucketStoneLava.setUnlocalizedName(INames.BUCKETSTONELAVA).setContainerItem(BucketLoader.bucketStone);
		
		bucketStoneLava.setRegistryName(INames.BUCKETSTONELAVA);
		bucketStoneLava.registerItems();
		
		bucketStoneDemonWater = new BucketStone(blockDemonWater, INames.BUCKETSTONEDMW);
		bucketStoneDemonWater.setUnlocalizedName(INames.BUCKETSTONEDMW).setContainerItem(BucketLoader.bucketStone);
		
		bucketStoneDemonWater.setRegistryName(INames.BUCKETSTONEDMW);
		bucketStoneDemonWater.registerItems();
		
		//Same with Wood
		
		bucketWoodWater = new BucketWood(Blocks.FLOWING_WATER, INames.BUCKETWOODWATER);
		bucketWoodWater.setUnlocalizedName(INames.BUCKETWOODWATER).setContainerItem(BucketLoader.bucketWood);
		
		bucketWoodWater.setRegistryName(INames.BUCKETWOODWATER);
		bucketWoodWater.registerItems();
		
		bucketWoodDemonWater = new BucketWood(blockDemonWater, INames.BUCKETWOODDMW);
		bucketWoodDemonWater.setUnlocalizedName(INames.BUCKETWOODDMW).setContainerItem(BucketLoader.bucketWood);
		
		bucketWoodDemonWater.setRegistryName(INames.BUCKETWOODDMW);
		bucketWoodDemonWater.registerItems();
		
		MinecraftForge.EVENT_BUS.register(BucketHandler.INSTANCE);
		
	}
}
