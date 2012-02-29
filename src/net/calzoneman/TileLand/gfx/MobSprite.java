package net.calzoneman.TileLand.gfx;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class MobSprite {
	public static final int FACING_UP = 0;
	public static final int FACING_RIGHT = 1;
	public static final int FACING_DOWN = 2;
	public static final int FACING_LEFT = 3;
	
	protected static final int WIDTH = 32;
	protected static final int HEIGHT = 42;
	
	private Texture texture;
	private int currentX;
	private int currentY;
	
	public MobSprite(Texture tex) {
		this.texture = tex;
		this.currentX = 0;
		this.currentY = FACING_DOWN * HEIGHT;
	}
	
	public int getWidth() {
		return WIDTH;
	}
	
	public int getHeight() {
		return HEIGHT;
	}
	
	public void setFacing(int facing) {
		if(facing * HEIGHT != currentY) {
			currentX = 0;
			currentY = facing * HEIGHT;
		}
	}
	
	public void nextFrame() {
		currentX += WIDTH;
		if(currentX >= texture.getImageWidth())
			currentX = 0;
	}
	
	public void resetFrame() {
		currentX = 0;
	}
	
	public void render(Screen screen, int x, int y) {
		screen.renderSubTexture(texture, new Rectangle(currentX, currentY, WIDTH, HEIGHT), x, y);
	}
}
