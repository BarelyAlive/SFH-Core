package mod.sfhcore.json;

import com.google.gson.JsonElement;

public class JsonHelper
{
	final JsonElement json;

	public JsonHelper(final JsonElement json) {
		this.json = json;
	}

	public boolean getBoolean(final String object) {
		return json.getAsJsonObject().get(object).getAsBoolean();
	}

	public boolean getNullableBoolean(final String object, final boolean def) {
		boolean ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsBoolean();

		return ret;
	}

	public boolean getNullableBoolean(final String object) {
		return getNullableBoolean(object, false);
	}

	public int getInteger(final String object) {
		return json.getAsJsonObject().get(object).getAsInt();
	}

	public int getNullableInteger(final String object, final int def) {
		int ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsInt();

		return ret;
	}

	public double getDouble(final String object) {
		return json.getAsJsonObject().get(object).getAsDouble();
	}

	public double getNullableDouble(final String object, final double def) {
		double ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsDouble();

		return ret;
	}


	public float getNullableFloat(final String object, final float def) {
		float ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsFloat();

		return ret;
	}

	public String getString(final String object) {
		return json.getAsJsonObject().get(object).getAsString();
	}

	public String getNullableString(final String object, final String def) {
		String ret = def;

		if (json.getAsJsonObject().get(object) != null)
			ret = json.getAsJsonObject().get(object).getAsString();

		return ret;
	}

}