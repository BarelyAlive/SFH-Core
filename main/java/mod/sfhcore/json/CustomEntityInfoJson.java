package mod.sfhcore.json;

import java.lang.reflect.Type;

import com.google.gson.*;

import mod.sfhcore.util.EntityInfo;

public class CustomEntityInfoJson implements JsonDeserializer<EntityInfo>, JsonSerializer<EntityInfo>
{
    @Override
    public JsonElement serialize(EntityInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
    	JsonElement prim = new JsonPrimitive((src.getName() == null ? src.toString() : src.getName()));
        return prim;
    }
    
    @Override
    public EntityInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
            return new EntityInfo(json.getAsString());
    }
}
