package mod.sfhcore.json;

import java.lang.reflect.Type;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import mod.sfhcore.util.EntityInfo;

public class CustomEntityInfoJson implements JsonDeserializer<EntityInfo>, JsonSerializer<EntityInfo>
{
	@Override
	public JsonElement serialize(final EntityInfo src, final Type typeOfSrc, final JsonSerializationContext context)
	{
		JsonElement prim = new JsonPrimitive(src.getName() == null ? src.toString() : src.getName());
		return prim;
	}

	@Override
	public EntityInfo deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
	{
		return new EntityInfo(json.getAsString());
	}
}
