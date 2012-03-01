package net.calzoneman.TileLand.gui;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.Screen;

public class GUICheckbox extends GUIComponent {
	
	public static final int WIDTH = 16;
	public static final int HEIGHT = WIDTH;
	
	static final Rectangle TEX_UNCHECKED = new Rectangle(0, 0, WIDTH, HEIGHT);
	static final Rectangle TEX_CHECKED = new Rectangle(0, HEIGHT, WIDTH, HEIGHT);
	
	private boolean checked;
	private Texture texture;

	public GUICheckbox(int x, int y) {
		super(x, y, WIDTH, HEIGHT);
		checked = false;
		texture = ResourceManager.GUI_CHECKBOX_TEXTURE;
	}
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	
	@Override
	public void onClick() {
		checked = !checked;
	}

	@Override
	public void render(Screen screen) {
		screen.renderSubTexture(texture, checked ? TEX_CHECKED : TEX_UNCHECKED, x, y);
	}

}
