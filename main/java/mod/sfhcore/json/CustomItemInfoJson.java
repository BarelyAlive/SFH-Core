package mod.sfhcore.json;

import java.lang.reflect.Type;

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
    public JsonElement serialize(ItemInfo src, Type typeOfSrc, JsonSerializationContext context)
    {
        JsonObject obj = new JsonObject();
        
        obj.addProperty("name", src.getItem().getRegistryName().toString());
        obj.addProperty("meta", src.getMeta());
        
        return obj;
    }
    
    @Override
    public ItemInfo deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException
    {
        JsonHelper helper = new JsonHelper(json);
        
        String name = helper.getString("name");
        int meta = helper.getNullableInteger("meta", 0);

        Item item = Item.getByNameOrId(name);
        
        if(item == null)
        {
            LogUtil.error("Error parsing JSON: Invalid Item: " + json.toString());
            LogUtil.error("This may result in crashing or other undefined behavior");
        }
        
        return new ItemInfo(item, meta);
    }
}
