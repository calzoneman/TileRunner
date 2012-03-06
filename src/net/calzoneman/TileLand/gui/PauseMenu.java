package net.calzoneman.TileLand.gui;

import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.util.Delegate;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class PauseMenu extends GUIMenu {
	
	static final Color backgroundColor = new Color(64, 64, 64);
	static final String pauseMessage = "GAME PAUSED";

	public PauseMenu() {
		super();
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	@Override
	public void init(int x, int y, int width, int height) {
		GUIContainer container = new GUIContainer(Display.getWidth()/2 - 320, Display.getHeight()/2 - 240, 640, 480);
		container.addChild("title", new GUIImage(container.getWidth()/2 - 256, 50, ResourceManager.TITLE_TEXTURE));

		GUIButton backBtn = new GUIButton(170, 260, 300, "Return to game");
		backBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		container.addChild("backbtn", backBtn);
		
		GUIButton quitBtn = new GUIButton(170, 300, 300, "Quit");
		quitBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().openMenu("mainmenu");
						return null;
					}
				});
		container.addChild("quitbtn", quitBtn);
		
		container.setParent(this);
		addChild("container", container);
	}
	
	@Override
	public void reInit(int x, int y, int width, int height) {
		children.clear();
		init(x, y, width, height);
	}
	
	@Override
	public void handleInput() {
		super.handleInput();
		//if(keys[Keyboard.KEY_ESCAPE]) {
		//	MenuManager.getMenuManager().goBack();
		//}
	}
	
	@Override
	public void render(Screen screen) {
		screen.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), backgroundColor);
		for(GUIComponent child : children.values()) {
			child.render(screen);
		}
		int sx = Display.getWidth() / 2 - Font.getWidthLarge(pauseMessage) / 2;
		int sy = Display.getHeight() / 2 - Font.getHeightLarge(pauseMessage) / 2;
		Font.drawLarge(pauseMessage, screen, sx, sy, backgroundColor);
	}
}
