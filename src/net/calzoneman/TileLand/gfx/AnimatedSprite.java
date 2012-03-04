package net.calzoneman.TileLand.gfx;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class AnimatedSprite extends Sprite {
	
	protected int frameWidth;
	protected int frameHeight;
	protected int numFrames;
	protected int delay;
	protected long lastFrameChange = -1;
	protected int fx = 0;

	public AnimatedSprite(Texture texture, int frameWidth, int delay) {
		super(texture);
		this.frameWidth = frameWidth;
		this.frameHeight = texture.getImageHeight();
		this.numFrames = texture.getImageWidth() / frameWidth;
		this.delay = delay;
	}
	
	public void nextFrame() {
		fx = (fx + 1) % numFrames;
	}
	
	public void prevFrame() {
		fx = (fx - 1) % numFrames;
	}
	
	public void reset() {
		fx = 0;
	}
	
	@Override
	public int getWidth() {
		return frameWidth;
	}
	
	@Override
	public void render(Screen screen, int x, int y) {
		if(System.currentTimeMillis() > lastFrameChange + delay) {
			nextFrame();
			lastFrameChange = System.currentTimeMillis();
		}
		screen.renderSubTexture(texture, 
				new Rectangle(fx * frameWidth, 0, frameWidth, frameHeight), 
				x, y);
	}

}
