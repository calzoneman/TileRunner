package net.calzoneman.TileLand.gui;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.util.Delegate;

public class GUIButton extends GUIComponent {
	
	public static final int BUTTON_HEIGHT = 30;
	static final int EDGE_WIDTH = 3;
	static final Rectangle LEFT_EDGE = new Rectangle(0, 0, 3, 30);
	static final Rectangle CENTER = new Rectangle(3, 0, 1, 30);
	static final Rectangle RIGHT_EDGE = new Rectangle(4, 0, 3, 30);
	
	static final Rectangle LEFT_EDGE_HOVER = new Rectangle(0, 30, 3, 30);
	static final Rectangle CENTER_HOVER = new Rectangle(3, 30, 1, 30);
	static final Rectangle RIGHT_EDGE_HOVER = new Rectangle(4, 30, 3, 30);
		
	protected GUIContainer parent;
	protected String text;
	protected Delegate<GUIContainer, Void> clickHandler;
	protected Texture texture;
	protected boolean enabled;

	public GUIButton(int x, int y, String text) {
		this(x, y, Font.getWidth(text) + 20, text);
	}
	
	public GUIButton(int x, int y, int width, String text) {
		this(x, y, width, text, null);
	}
	
	public GUIButton(int x, int y, String text, GUIContainer parent) {
		this(x, y, Font.getWidth(text) + 20, text, parent);
	}
	
	public GUIButton(int x, int y, int width, String text, GUIContainer parent) {
		super(x, y, width, BUTTON_HEIGHT);
		this.text = text;
		this.parent = parent;
		this.texture = ResourceManager.GUI_BUTTON_TEXTURE;
		this.enabled = true;
	}
	
	public boolean isEnabled() {
		return enabled;
	}
	
	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}
	
	public void setParent(GUIContainer parent) {
		this.parent = parent;
	}
	
	public GUIContainer getParent() {
		return this.parent;
	}
	
	public void setClickHandler(Delegate<GUIContainer, Void> clickHandler) {
		this.clickHandler = clickHandler;
	}
	
	public void setText(String text) {
		this.text = text;
	}
	
	public String getText() {
		return this.text;
	}

	@Override
	public void render(Screen screen) {
		if(isHovered() && isEnabled()) {
			// Draw left edge
			screen.renderSubTexture(texture, LEFT_EDGE_HOVER, x, y);
			// Render center
			screen.renderSubTexture(texture, CENTER_HOVER, x+EDGE_WIDTH, y, width-2*EDGE_WIDTH, height);
			// Draw right edge
			screen.renderSubTexture(texture, RIGHT_EDGE_HOVER, x+width-EDGE_WIDTH, y);
		}
		else {
			// Draw left edge
			screen.renderSubTexture(texture, LEFT_EDGE, x, y);
			// Render center
			screen.renderSubTexture(texture, CENTER, x+EDGE_WIDTH, y, width-2*EDGE_WIDTH, height);
			// Draw right edge
			screen.renderSubTexture(texture, RIGHT_EDGE, x+width-EDGE_WIDTH, y);
		}
		int w = Font.getWidth(text);
		int h = Font.getHeight(text);
		int sx = this.x + this.width/2 - w/2;
		int sy = this.y + this.height/2 - h/2;
		if(isFocused() && isEnabled())
			Font.draw(Font.TEXT_YELLOW + text, screen, sx, sy);
		else if(!isEnabled())
			Font.draw(Font.TEXT_GRAY + text, screen, sx, sy);
		else
			Font.draw(text, screen, sx, sy);	
	}

	@Override
	public void onClick() {
		if(!enabled)
			return;
		if(clickHandler != null)
			clickHandler.run(parent);
		blur();
	}

}
