package net.calzoneman.TileLand.gfx;

import org.newdawn.slick.opengl.Texture;

public class Sprite {
	protected Texture texture;
	
	public Sprite(Texture texture) {
		this.texture = texture;
	}
	
	public void render(Screen screen, int x, int y) {
		screen.renderTexture(texture, x, y);
	}
	
	public int getWidth() {
		return texture.getImageWidth();
	}
	
	public int getHeight() {
		return texture.getImageHeight();
	}
}
