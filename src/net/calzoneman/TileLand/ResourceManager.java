package net.calzoneman.TileLand;

import java.io.IOException;

import net.calzoneman.TileLand.gfx.SpriteSheet;

import org.newdawn.slick.SlickException;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.font.effects.ColorEffect;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

@SuppressWarnings("unchecked")
public class ResourceManager {
	
	public static Texture GUI_BUTTON_TEXTURE;
	public static Texture GUI_TEXTBOX_TEXTURE;
	public static Texture GUI_CHECKBOX_TEXTURE;
	public static Texture PLAYER_TEXTURE;
	public static Texture TITLE_TEXTURE;
	public static SpriteSheet TILESHEET;
	public static UnicodeFont FONT;
	public static UnicodeFont FONT_LARGE;
	
	static {
		try {
			GUI_BUTTON_TEXTURE = TextureLoader.getTexture("PNG", ResourceManager.class.getResourceAsStream("/gui_button.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			GUI_TEXTBOX_TEXTURE = TextureLoader.getTexture("PNG", ResourceManager.class.getResourceAsStream("/gui_textbox.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			GUI_CHECKBOX_TEXTURE = TextureLoader.getTexture("PNG", ResourceManager.class.getResourceAsStream("/gui_checkbox.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			PLAYER_TEXTURE = TextureLoader.getTexture("PNG", ResourceManager.class.getResourceAsStream("/player.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			TITLE_TEXTURE = TextureLoader.getTexture("PNG", ResourceManager.class.getResourceAsStream("/title.png"));
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			TILESHEET = new SpriteSheet(ResourceManager.class.getResourceAsStream("/tiles.png"), 32);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FONT = new UnicodeFont(java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceManager.class.getResourceAsStream("/font.ttf")), 8, false, false);
			FONT.addAsciiGlyphs();
			FONT.getEffects().add(new ColorEffect()); // For some reason you have to add an effect...
			FONT.loadGlyphs();
			
			FONT_LARGE = new UnicodeFont(java.awt.Font.createFont(java.awt.Font.TRUETYPE_FONT, ResourceManager.class.getResourceAsStream("/font.ttf")), 16, false, false);
			FONT_LARGE.addAsciiGlyphs();
			FONT_LARGE.getEffects().add(new ColorEffect()); // For some reason you have to add an effect...
			FONT_LARGE.loadGlyphs();
		} 
		catch (SlickException e) {
			e.printStackTrace();
		} 
		catch (Exception e) {
			e.printStackTrace();
		}
	}

}
