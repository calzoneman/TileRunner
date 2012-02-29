package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.gfx.Screen;

import org.newdawn.slick.opengl.Texture;

public class GUIImage extends GUIComponent {
	protected Texture texture;

	public GUIImage(int x, int y, Texture tex) {
		super(x, y, tex.getTextureWidth(), tex.getTextureHeight());
		this.texture = tex;
	}

	@Override
	public void render(Screen screen) {
		screen.renderTexture(texture, x, y, width, height, false, false);
	}

}
