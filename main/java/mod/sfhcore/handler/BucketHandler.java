package mod.sfhcore.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import mod.sfhcore.item.CustomBucket;
import net.minecraft.client.renderer.block.model.ModelBakery;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelDynBucket;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.event.RegistryEvent.Register;
import net.minecraftforge.fluids.BlockFluidBase;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.registries.IForgeRegistry;

class StringFluid {
	String str;
	Fluid f;
}

class BucketInfo {
	Integer color;
	Integer max_stack_size_for_empty_bucket;
	String mod_id;
	String material_name;
	Integer highest_temperatur;
	CreativeTabs tab;
}

public class BucketHandler {
	private static final Map<String, BucketInfo> BUCKET_LIST = new HashMap<>();
	private static final Map<CustomBucket, StringFluid> ALL_BUCKET_LIST = new HashMap<>();
	private static final Map<String, ArrayList<String>> DISABLED_BUCKETS = new HashMap<>();


	public static void addBucket(final String material, final String material_name, final int highest_temperatur, final int max_stack_size_for_empty_bucket, final String mod_id, final int bucket_color, final CreativeTabs tab)
	{
		final BucketInfo info = new BucketInfo();
		info.color = bucket_color;
		info.mod_id = mod_id;
		info.max_stack_size_for_empty_bucket = max_stack_size_for_empty_bucket;
		info.tab = tab;
		info.highest_temperatur = highest_temperatur;
		info.material_name = material_name;
		BUCKET_LIST.put(material, info);
	}

	public static void disabledBucket(final String material, final String fluid_id)
	{
		if (!DISABLED_BUCKETS.containsKey(material))
			DISABLED_BUCKETS.put(material, new ArrayList<>());

		if (!DISABLED_BUCKETS.get(material).contains(fluid_id))
			DISABLED_BUCKETS.get(material).add(fluid_id);
	}

	public static void registerBuckets(final Register<Item> event)
	{
		if(BUCKET_LIST.size() == 0)
			return;
		addAllBuckets();
		registerItem(event.getRegistry());
	}

	public static CustomBucket getBucketFromFluid(final Fluid searchFluid, final String material)
	{
		for(final CustomBucket item : ALL_BUCKET_LIST.keySet())
			if (item != null)
			{
				if(item.getFluid() != searchFluid)
					continue;
				final StringFluid fl = ALL_BUCKET_LIST.get(item);
				if(!fl.str.contains(material))
					continue;
				return item;
			}

		return null;
	}
	
	public static int getMaxTemperaturFromBucket(final String material)
	{
		for(final String item : BUCKET_LIST.keySet())
			if (item != null)
				if (item.equals(material))
				{
					final BucketInfo bi = BUCKET_LIST.get(item);
					if (bi != null)
						return bi.highest_temperatur;
				}

		return 0;
	}

	public static String getMaterialFromBucket(final ItemStack stack)
	{
		if (stack.isEmpty())
			return "";
		if (!(stack.getItem() instanceof CustomBucket))
			return "";
		return getMaterialFromBucket((CustomBucket) stack.getItem());
	}

	public static String getMaterialFromBucket(final CustomBucket bucket)
	{
		return bucket.getMaterial();
	}

	private static void addAllBuckets()
	{
		final Map<String, Fluid> fluids = FluidRegistry.getRegisteredFluids();
		Fluid f;
		String resource_domain;
		String resource_path;
		ResourceLocation res_loc;
		CustomBucket new_bucket;
		CustomBucket bucket_0;
		ItemStack emptyBucket;
		StringFluid strfld = new StringFluid();
		ResourceLocation bucketName;

		for(final String material : BUCKET_LIST.keySet())
		{
			final BucketInfo bucket_info = BUCKET_LIST.get(material);
			bucketName = new ResourceLocation(bucket_info.mod_id, "bucket_" + material);
			bucket_0 = new CustomBucket(Blocks.AIR, bucketName, ItemStack.EMPTY, bucket_info.tab, bucket_info.color, material);
			bucket_0.setMaxStackSize(bucket_info.max_stack_size_for_empty_bucket);
			strfld = new StringFluid();
			strfld.f = null;
			strfld.str = material;
			bucket_0.setLocalizedName(bucket_info.material_name + " " + Items.BUCKET.getItemStackDisplayName(new ItemStack(Items.BUCKET)));
			ALL_BUCKET_LIST.put(bucket_0, strfld);
			emptyBucket = new ItemStack(bucket_0);
			for(final String name : fluids.keySet())
			{
				f = fluids.get(name);
				if (DISABLED_BUCKETS.containsKey(material))
					if (
							DISABLED_BUCKETS.get(material).contains(name)
							|| DISABLED_BUCKETS.get(material).contains(f.getName())
							|| DISABLED_BUCKETS.get(material).contains(f.getUnlocalizedName())
							|| DISABLED_BUCKETS.get(material).contains(f.getLocalizedName(new FluidStack(f, 1000)))
							|| DISABLED_BUCKETS.get(material).contains(Objects.requireNonNull(f.getBlock().getRegistryName()).getResourcePath())
							|| DISABLED_BUCKETS.get(material).contains(f.getBlock().getRegistryName().getResourcePath() + ":" + f.getBlock().getRegistryName().getResourcePath()))
						continue;

				if (!( bucket_info.highest_temperatur == -1 || bucket_info.highest_temperatur == 0))
					if(f.getTemperature() >= bucket_info.highest_temperatur)
						continue;

				if (f.getBlock() == null)
					continue;
				if (f.getBlock().equals(Blocks.AIR))
					continue;
				if (!(f.getBlock() instanceof BlockFluidBase) && !(f.equals(FluidRegistry.WATER) || f.equals(FluidRegistry.LAVA)))
					continue;

				resource_domain = Objects.requireNonNull(bucket_0.getRegistryName()).getResourceDomain();
				resource_path = bucket_0.getRegistryName().getResourcePath() + "_" + name;
				res_loc = new ResourceLocation(resource_domain, resource_path);

				new_bucket = new CustomBucket(f.getBlock(), res_loc, emptyBucket, bucket_info.tab, bucket_info.color, material);
				new_bucket.setMaxStackSize(1);

				strfld = new StringFluid();
				strfld.f = f;
				strfld.str = material;

				new_bucket.setLocalizedName(f.getLocalizedName(new FluidStack(f, 1000)) + " " + bucket_info.material_name + " " + Items.BUCKET.getItemStackDisplayName(new ItemStack(Items.BUCKET)));
				ALL_BUCKET_LIST.put(new_bucket, strfld);
			}
		}
	}

	private static void registerItem(final IForgeRegistry<Item> registry)
	{
		for(final CustomBucket item : ALL_BUCKET_LIST.keySet())
			if (item != null && item.getRegistryName() != null)
				registry.register(item);

	}

	@SideOnly(Side.CLIENT)
	public static void registerBucketModels(final ModelRegistryEvent registry)
	{
		for(final CustomBucket item : ALL_BUCKET_LIST.keySet())
		{
			final StringFluid strfld = ALL_BUCKET_LIST.get(item);
			final String material = strfld.str;
			//String material = allBucketList.get(item);
			if (item != null && item.getRegistryName() != null)
			{
				final String name = "bucket_";
				final String domain = item.getRegistryName().getResourceDomain();
				final ResourceLocation baseLocation = null;
				final ResourceLocation liquidLocation = null;
				final ResourceLocation coverLocation = null;
				final Fluid fluid = strfld.f;
				final net.minecraftforge.client.model.ModelDynBucket bucketModel = new net.minecraftforge.client.model.ModelDynBucket(null, null, null, fluid, false, true);
				//bucketModel.LOCATION = new ModelResourceLocation(new ResourceLocation("sfhcore", "bucket"), "inventory");
				ModelLoaderRegistry.registerLoader(net.minecraftforge.client.model.ModelDynBucket.LoaderDynBucket.INSTANCE);
				ModelLoader.setCustomMeshDefinition(item, stack -> ModelDynBucket.LOCATION);
				//ModelLoader.setCustomModelResourceLocation(item, 0, bucketModel.LOCATION);
				ModelBakery.registerItemVariants(item, ModelDynBucket.LOCATION);
			}
		}
	}

	@SideOnly(Side.CLIENT)
	public static void registerItemHandlers(final net.minecraftforge.client.event.ColorHandlerEvent.Item event) {
		for(final CustomBucket item : ALL_BUCKET_LIST.keySet())
		{
			final StringFluid strfld = ALL_BUCKET_LIST.get(item);
			final String material = strfld.str;
			if (item != null && item.getRegistryName() != null)
				event.getItemColors().registerItemColorHandler(new mod.sfhcore.item.model_bucket.FluidCustomBucketColorer(), item);
		}
	}
}
