package mod.sfhcore.client.renderer;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SpriteColor {
	private TextureAtlasSprite sprite;
	private mod.sfhcore.texturing.Color color;

	public SpriteColor(final TextureAtlasSprite sprite, final mod.sfhcore.texturing.Color color2)
	{
		this.sprite = sprite;
		color = color2;
	}

	public TextureAtlasSprite getSprite()
	{
		return sprite;
	}

	public mod.sfhcore.texturing.Color getColor()
	{
		return color;
	}

}