package mod.sfhcore.client.renderers;

import java.util.Objects;

public class Color {
	public static final Color INVALID_COLOR = new Color(-1, -1, -1, -1);

	public final float r;
	public final float g;
	public final float b;
	public final float a;

	public Color(final float red, final float green, final float blue, final float alpha) {
		r = red;
		g = green;
		b = blue;
		a = alpha;
	}

	public Color(final int color) {
		//stupid minecraft color is RGB not ARGB.
		//need to simulate the Alpha value.
		//this.a = (float) (color >> 24 & 255) / 255.0F;
		this(color, true);
	}

	public Color(final int color, final boolean ignoreAlpha) {
		if (ignoreAlpha) {
			a = 1.0f;
			r = (color >> 16 & 255) / 255.0F;
			g = (color >> 8 & 255) / 255.0F;
			b = (color & 255) / 255.0F;
		} else {
			a = (color >> 24 & 255) / 255.0F;
			r = (color >> 16 & 255) / 255.0F;
			g = (color >> 8 & 255) / 255.0F;
			b = (color & 255) / 255.0F;
		}
	}

	public Color(final String hex) {
		this(Integer.parseInt(hex, 16));
	}

	public static Color average(final Color colorA, final Color colorB, final float percentage) {
		final float opposite = 1 - percentage;
		//Gamma correction

		final float averageR = (float) Math.sqrt(colorA.r * colorA.r * opposite + colorB.r * colorB.r * percentage);
		final float averageG = (float) Math.sqrt(colorA.g * colorA.g * opposite + colorB.r * colorB.g * percentage);
		final float averageB = (float) Math.sqrt(colorA.b * colorA.b * opposite + colorB.r * colorB.b * percentage);
		final float averageA = colorA.a * opposite + colorB.a * percentage;

		return new Color(averageR, averageG, averageB, averageA);
	}

	public int toInt() {
		int color = 0;
		color |= (int) (a * 255) << 24;
		color |= (int) (r * 255) << 16;
		color |= (int) (g * 255) << 8;
		color |= (int) (b * 255);
		return color;
	}

	public int toIntNoAlpha() {
		int color = 0;
		color |= (int) (r * 255) << 16;
		color |= (int) (g * 255) << 8;
		color |= (int) (b * 255);
		return color;
	}

	public String getAsHexNoAlpha(){
		return Integer.toHexString(toIntNoAlpha());
	}

	@Override
	public boolean equals(final Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		final Color color = (Color) o;
		return Float.compare(color.r, r) == 0 &&
				Float.compare(color.g, g) == 0 &&
				Float.compare(color.b, b) == 0 &&
				Float.compare(color.a, a) == 0;
	}

	@Override
	public int hashCode() {

		return Objects.hash(r, g, b, a);
	}

	@Override
	public String toString() {
		return "Color{" +
				"r=" + r +
				", g=" + g +
				", b=" + b +
				", a=" + a +
				'}';
	}
}