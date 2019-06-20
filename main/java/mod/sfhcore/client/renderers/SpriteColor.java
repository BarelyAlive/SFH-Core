package mod.sfhcore.client.renderers;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SpriteColor {
    private TextureAtlasSprite sprite;
    private mod.sfhcore.texturing.Color color;
    
    public SpriteColor(TextureAtlasSprite sprite, mod.sfhcore.texturing.Color color2)
    {
    	this.sprite = sprite;
    	this.color = color2;
    }
    
    public TextureAtlasSprite getSprite()
    {
    	return this.sprite;
    }
    
    public mod.sfhcore.texturing.Color getColor()
    {
    	return this.color;
    }
    
}