package mod.sfhcore.json;

import java.lang.reflect.Type;
import java.util.Objects;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mod.sfhcore.util.ItemInfo;
import mod.sfhcore.util.LogUtil;
import net.minecraft.item.Item;

public class CustomItemInfoJson implements JsonDeserializer<ItemInfo>, JsonSerializer<ItemInfo>
{
	public static final CustomItemInfoJson INSTANCE = new CustomItemInfoJson();

	@Override
	public JsonElement serialize(final ItemInfo src, final Type typeOfSrc, final JsonSerializationContext context)
	{
		final JsonObject obj = new JsonObject();

		obj.addProperty("name", Objects.requireNonNull(src.getItem().getRegistryName()).toString());
		obj.addProperty("meta", src.getMeta());

		return obj;
	}

	@Override
	public ItemInfo deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
	{
		final JsonHelper helper = new JsonHelper(json);

		final String name = helper.getString("name");
		final int meta = helper.getNullableInteger("meta", 0);

		final Item item = Item.getByNameOrId(name);

		if(item == null)
		{
			LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
			LogUtil.error("This may result in crashing or other undefined behavior");
		}

		return new ItemInfo(Objects.requireNonNull(item), meta);
	}
}
