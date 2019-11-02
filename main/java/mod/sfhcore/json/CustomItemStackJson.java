package mod.sfhcore.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mod.sfhcore.util.LogUtil;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.JsonToNBT;
import net.minecraft.nbt.NBTException;
import net.minecraft.nbt.NBTTagCompound;

public class CustomItemStackJson implements JsonDeserializer<ItemStack>, JsonSerializer<ItemStack>
{
	@Override
	public ItemStack deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
	{
		final JsonHelper helper = new JsonHelper(json);

		final String name = helper.getString("name");
		final int amount = helper.getNullableInteger("amount", 1);
		final int meta = helper.getNullableInteger("meta", 0);

		Item item = Item.getByNameOrId(name);
		if(item == null){
			LogUtil.error("Error parsing JSON: Invalid Item: $json");
			LogUtil.error("This may result in crashing or other undefined behavior");

			item = Items.AIR;
		}

		final ItemStack stack = new ItemStack(item, amount, meta);

		if (json.getAsJsonObject().has("nbt"))
			try
		{
				stack.setTagCompound(JsonToNBT.getTagFromJson(json.getAsJsonObject().get("nbt").getAsString()));
		}
		catch (final NBTException e)
		{
			LogUtil.error("Could not convert JSON to NBT: " + json.getAsJsonObject().get("nbt").getAsString());
			e.printStackTrace();
		}

		return stack;
	}

	@Override
	public JsonElement serialize(final ItemStack src,final Type typeOfSrc, final JsonSerializationContext context)
	{
		final JsonObject jsonObject = new JsonObject();

		jsonObject.addProperty("name", src.getItem().getRegistryName() == null ?  "" : src.getItem().getRegistryName().toString());
		jsonObject.addProperty("amount", src.getCount());
		jsonObject.addProperty("meta", src.getItemDamage());

		final NBTTagCompound nbt = src.getTagCompound();
		if (nbt != null && !nbt.hasNoTags())
			jsonObject.addProperty("nbt", nbt.toString());

		return jsonObject;
	}
}