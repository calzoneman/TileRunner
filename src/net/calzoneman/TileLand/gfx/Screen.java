package net.calzoneman.TileLand.gfx;

import net.calzoneman.TileLand.TileLand;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

import org.newdawn.slick.Color;
import org.newdawn.slick.UnicodeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

public class Screen {
	
	public boolean initialized;
	
	/** FPS counter variables */
	private long lastFPSMeasureTime;
	private long currentFrames;
	private int fps;
	
	private int offX, offY;

	/**
	 * Initializes the Renderer, loads appropriate resources, initializes GL
	 */
	public Screen(int width, int height) {
		if (!initGL(width, height))
			initialized = false;
		else {
			currentFrames = 0;
			fps = 0;
			lastFPSMeasureTime = System.nanoTime() - 1000000000;
			offX = offY = 0;
			initialized = true;
		}
	}

	/**
	 * Internal function to handle GL initialization
	 * 
	 * @param width
	 *            The desired screen width
	 * @param height
	 *            The desired screen height
	 */
	private boolean initGL(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.setTitle("TileRunner " + TileLand.version);
			Display.create();
			Display.setVSyncEnabled(true);
		} catch (Exception ex) {
			System.out.println("Error initializing GL: ");
			ex.printStackTrace();
			return false;
		}

		glEnable(GL_TEXTURE_2D);
		// Set the background color to black
		glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
		// Enable Alpha
		glEnable(GL_SRC_ALPHA);
		glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		// Setup the view
		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);

		return true;
	}

	public boolean reInit(int width, int height) {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
		} catch (Exception ex) {
			System.out.println("Error initializing GL: ");
			ex.printStackTrace();
			return false;
		}

		glViewport(0, 0, width, height);
		glMatrixMode(GL_PROJECTION);
		glLoadIdentity();
		glOrtho(0, width, height, 0, 1, -1);
		glMatrixMode(GL_MODELVIEW);
		return true;
	}
	
	public void setOffset(int xo, int yo) {
		offX = xo;
		offY = yo;
	}
	
	public int getOffsetX() {
		return offX;
	}
	
	public int getOffsetY() {
		return offY;
	}

	public void renderFPS() {
		if(!initialized)
			return;
		glEnable(GL_BLEND);
		Font.draw("FPS: " + fps, this, 0, 0, Color.black);
		glDisable(GL_BLEND);
		currentFrames++;
		if (System.nanoTime() >= lastFPSMeasureTime + 1000000000) {
			fps = (int) currentFrames;
			currentFrames = 0;
			lastFPSMeasureTime = System.nanoTime();
		}
	}

	public void renderTexture(Texture tex, int x, int y) {
		if(tex == null)
			return;
		renderTexture(tex, x, y, tex.getImageWidth(), tex.getImageHeight(), false, false);
	}

	public void renderTexture(Texture tex, int x, int y, int w, int h, boolean flipX, boolean flipY) {
		if (tex == null || !initialized)
			return;
		
		x -= offX;
		y -= offY;
		
		double tx0 = flipX ? 1.0D : 0.0D;
		double tx1 = flipX ? 0.0D : 1.0D;
		double ty0 = flipY ? 1.0D : 0.0D;
		double ty1 = flipY ? 0.0D : 1.0D;
		
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
			glTexCoord2d(tx0, ty0);
			glVertex2i(x,  y);
			
			glTexCoord2d(tx1, ty0);
			glVertex2i(x + w,  y);
			
			glTexCoord2d(tx1, ty1);
			glVertex2i(x + w,  y + h);
			
			glTexCoord2d(tx0, ty1);
			glVertex2i(x,  y + h);
		glEnd();
		glDisable(GL_BLEND);
	}

	
	public void renderSubTexture(Texture tex, Rectangle rect, int x, int y) {
		if(tex == null || rect == null)
			return;
		renderSubTexture(tex, rect, x, y, (int) rect.getWidth(), (int) rect.getHeight(), false, false);
	}
	
	public void renderSubTexture(Texture tex, Rectangle rect, int x, int y, int w, int h) {
		renderSubTexture(tex, rect, x, y, w, h, false, false);
	}

	public void renderSubTexture(Texture tex, Rectangle rect, int x, int y, int w, int h, boolean flipX, boolean flipY) {
		if(tex == null || rect == null)
			return;
		x -= offX;
		y -= offY;
		
		int texWidth = tex.getTextureWidth();
		int texHeight = tex.getTextureHeight();
		float rectX = rect.getX();
		float rectY = rect.getY();
		float rectWidth = rect.getWidth();
		float rectHeight = rect.getHeight();
		
		double tx0 = flipX ? (rectX + rectWidth) / texWidth : rectX / texWidth;
		double tx1 = flipX ? rectX / texWidth : (rectX + rectWidth) / texWidth;
		double ty0 = flipY ? (rectY + rectHeight) / texHeight : rectY / texHeight;
		double ty1 = flipY ? rectY / texHeight : (rectY + rectHeight) / texHeight;
		
		tex.bind();
		glEnable(GL_BLEND);
		glBegin(GL_QUADS);
			glTexCoord2d(tx0, ty0);
			glVertex2f(x,  y);
			
			glTexCoord2d(tx1, ty0);
			glVertex2f(x + w,  y);
			
			glTexCoord2d(tx1, ty1);
			glVertex2f(x + w,  y + h);
			
			glTexCoord2d(tx0, ty1);
			glVertex2f(x,  y + h);
		glEnd();
		glDisable(GL_BLEND);
	}

	public void renderFilledRect(int x, int y, int w, int h, Color col) {
		x -= offX;
		y -= offY;
		glDisable(GL_TEXTURE_2D);
		if (col.getAlpha() != 255)
			glEnable(GL_BLEND);
		col.bind();
		glBegin(GL_QUADS);
			glVertex2f(x,  y);
			glVertex2f(x + w,  y);
			glVertex2f(x + w,  y + h);
			glVertex2f(x,  y + h);
		glEnd();
		if (col.getAlpha() != 255)
			glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}

	public void renderRect(int x, int y, int w, int h, Color col) {
		x -= offX;
		y -= offY;
		glDisable(GL_TEXTURE_2D);
		if (col.getAlpha() != 255)
			glEnable(GL_BLEND);
		col.bind();
		glBegin(GL_LINE_LOOP);
			glVertex2f(x,  y);
			glVertex2f(x + w,  y);
			glVertex2f(x + w,  y + h);
			glVertex2f(x,  y + h);
		glEnd();
		if (col.getAlpha() != 255)
			glDisable(GL_BLEND);
		glEnable(GL_TEXTURE_2D);
		// Reset the color to white
		glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
	}
	
	public void renderString(String str, UnicodeFont fnt, int x, int y) {
		x -= offX;
		y -= offY;
		glEnable(GL_BLEND);
			fnt.drawString(x, y, str);
		glDisable(GL_BLEND);
	}
}
