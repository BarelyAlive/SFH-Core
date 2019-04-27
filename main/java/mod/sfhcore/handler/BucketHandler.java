package mod.sfhcore.handler;

import java.util.*;

import mod.sfhcore.items.CustomBucket;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent.Register;

public class BucketHandler {
	
	private static List<CustomBucket> bucketList = new ArrayList<CustomBucket>();
	private static List<CustomBucket> allBucketList = new ArrayList<CustomBucket>();
	

	public static void addBucket(CustomBucket bucket)
	{
		bucketList.add(bucket);
	}

	public static void registerBuckets(Register<Item> event) {
	}

	public static void registerBucketModels(ModelRegistryEvent event) {
	}
	
	
	
}
