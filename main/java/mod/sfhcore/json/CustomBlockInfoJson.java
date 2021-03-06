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

import mod.sfhcore.util.BlockInfo;
import mod.sfhcore.util.LogUtil;
import net.minecraft.block.Block;

public class CustomBlockInfoJson implements JsonDeserializer<BlockInfo>, JsonSerializer<BlockInfo>
{
	public static final CustomBlockInfoJson INSTANCE = new CustomBlockInfoJson();

	@Override
	public JsonElement serialize(final BlockInfo src, final Type typeOfSrc, final JsonSerializationContext context)
	{
		final JsonObject obj = new JsonObject();

		obj.addProperty("name", Objects.requireNonNull(src.getBlock().getRegistryName()).toString());
		obj.addProperty("meta", src.getMeta());

		return obj;
	}

	@Override
	public BlockInfo deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException
	{
		final JsonHelper helper = new JsonHelper(json);

		final String name = helper.getString("name");
		final int meta = helper.getNullableInteger("meta", 0);

		final Block block = Block.getBlockFromName(name);

		if(block == null)
		{
			LogUtil.error("Error parsing JSON: Invalid Block: " + json.toString());
			LogUtil.error("This may result in crashing or other undefined behavior");
		}

		return new BlockInfo(Objects.requireNonNull(block), meta);
	}
}
