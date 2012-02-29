package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.gfx.Screen;

public class GUILabel extends GUIComponent {
	
	protected String text;

	public GUILabel(int x, int y, String text) {
		super(x, y, Font.getWidth(text), Font.getHeight(text));
		this.text = text;
	}
	
	@Override
	public int getWidth() {
		return Font.getWidth(text);
	}
	
	public String getText() {
		return text;
	}
	
	public void setText(String text) {
		this.text = text;
		this.setSize(Font.getWidth(text), Font.getHeight(text));
	}

	@Override
	public void render(Screen screen) {
		Font.draw(text, screen, x, y);
	}

}
