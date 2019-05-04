package mod.sfhcore.handler;

import java.util.*;

import mod.sfhcore.items.CustomBucket;
import mod.sfhcore.items.model_bucket.FluidCustomBucketColorer;
import mod.sfhcore.items.model_bucket.ModelDynCustomBucket;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.item.*;
import net.minecraft.util.*;
import net.minecraftforge.client.FluidContainerColorer;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.common.ForgeModContainer;
import net.minecraftforge.common.ForgeVersion;
import net.minecraftforge.event.RegistryEvent.*;
import net.minecraftforge.fluids.*;
import net.minecraftforge.fluids.capability.templates.FluidHandlerItemStack;
import net.minecraftforge.registries.IForgeRegistry;

class StringFluid {
	String str;
	Fluid f;
}

class BucketInfo {
	Integer color;
	CreativeTabs tab;
}

public class BucketHandler {
	private static Map<String, BucketInfo> bucketList = new HashMap<String, BucketInfo>();
	private static Map<CustomBucket, StringFluid> allBucketList = new HashMap<CustomBucket, StringFluid>();
	

	public static void addBucket(String material, Integer bucket_color, CreativeTabs tab)
	{
		BucketInfo info = new BucketInfo();
		info.color = bucket_color;
		info.tab = tab;
		bucketList.put(material, info);
	}

	public static void registerBuckets(Register<Item> event)
	{
		if(bucketList.size() == 0)
		{
			return;
		}
		addAllBuckets();
		registerItem(event.getRegistry());
	}
	
	private static void addAllBuckets()
	{
		Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
		Fluid f;
		String resource_domain;
		String resource_path;
		ResourceLocation res_loc;
		CustomBucket new_bucket;
		CustomBucket bucket_0;
		ItemStack emptyBucket;
		StringFluid strfld = new StringFluid();
		ResourceLocation bucketName;
		
		for(String material : bucketList.keySet())
		{
			BucketInfo bucket_info = bucketList.get(material);
			bucketName = new ResourceLocation("sfhcore", "bucket_" + material);
			bucket_0 = new CustomBucket(Blocks.AIR, bucketName, ItemStack.EMPTY, bucket_info.tab, bucket_info.color);
			strfld = new StringFluid();
			strfld.f = null;
			strfld.str = material;
			allBucketList.put(bucket_0, strfld);
			for(String name : fluids.keySet())
			{
				f = fluids.get(name);
				resource_domain = bucket_0.getRegistryName().getResourceDomain();
				resource_path = bucket_0.getRegistryName().getResourcePath() + "_" + name;
				res_loc = new ResourceLocation(resource_domain, resource_path);
				emptyBucket = new ItemStack(bucket_0);
				
				new_bucket = new CustomBucket(f.getBlock(), res_loc, emptyBucket, bucket_info.tab, bucket_info.color);
				
				strfld = new StringFluid();
				strfld.f = f;
				strfld.str = material;
				
				allBucketList.put(new_bucket, strfld);
			}
		}
	}
	
	private static void registerItem(IForgeRegistry<Item> registry)
	{
		for(CustomBucket item : allBucketList.keySet())
		{
			if (item != null && item.getRegistryName() != null)
			{
				registry.register(item);
			}
		}

	}

	public static void registerBucketModels(ModelRegistryEvent registry)
	{
		for(CustomBucket item : allBucketList.keySet())
		{
			StringFluid strfld = allBucketList.get(item);
			String material = strfld.str;
			//String material = allBucketList.get(item);
			if (item != null && item.getRegistryName() != null)
			{
				String name = "bucket_";
				name += material;
				String domain = item.getRegistryName().getResourceDomain();
				ResourceLocation baseLocation = null;
				ResourceLocation liquidLocation = null;
				ResourceLocation coverLocation = null;
				Fluid fluid = strfld.f;
				ModelDynCustomBucket bucketModel = new ModelDynCustomBucket(fluid, false, true);
				//bucketModel.LOCATION = new ModelResourceLocation(new ResourceLocation("sfhcore", "bucket"), "inventory");
				ModelLoaderRegistry.registerLoader(ModelDynCustomBucket.LoaderDynCustomBucket.INSTANCE);
				ModelLoader.setCustomMeshDefinition(item, stack -> bucketModel.LOCATION);
				//ModelLoader.setCustomModelResourceLocation(item, 0, bucketModel.LOCATION);
				ModelBakery.registerItemVariants(item, bucketModel.LOCATION);
			}
		}
	}

	public static void registerItemHandlers(net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
		for(CustomBucket item : allBucketList.keySet())
		{
			StringFluid strfld = allBucketList.get(item);
			String material = strfld.str;
			if (item != null && item.getRegistryName() != null)
			{
				event.getItemColors().registerItemColorHandler(new FluidCustomBucketColorer(), item);
			}
		}
	}	
}
