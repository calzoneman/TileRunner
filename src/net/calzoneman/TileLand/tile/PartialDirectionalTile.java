package net.calzoneman.TileLand.tile;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;

public class PartialDirectionalTile extends Tile {
	
	static final Rectangle TOP_CORNER_LEFT = new Rectangle(0, 0, TILESIZE, TILESIZE);
	static final Rectangle TOP_EDGE = new Rectangle(TILESIZE, 0, TILESIZE, TILESIZE);
	static final Rectangle TOP_CORNER_RIGHT = new Rectangle(2*TILESIZE, 0, TILESIZE, TILESIZE);
	
	static final Rectangle LEFT_EDGE = new Rectangle(0, TILESIZE, TILESIZE, TILESIZE);
	static final Rectangle CENTER = new Rectangle(TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	static final Rectangle RIGHT_EDGE = new Rectangle(2*TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	
	static final Rectangle BOTTOM_CORNER_LEFT = new Rectangle(0, 2*TILESIZE, TILESIZE, TILESIZE);
	static final Rectangle BOTTOM_EDGE = new Rectangle(TILESIZE, 2*TILESIZE, TILESIZE, TILESIZE);
	static final Rectangle BOTTOM_CORNER_RIGHT = new Rectangle(2*TILESIZE, 2*TILESIZE, TILESIZE, TILESIZE);
	
	protected Texture multitexture;

	public PartialDirectionalTile(short id, String name) {
		super(id, name);
	}
	
	public PartialDirectionalTile(short id, String name, Texture texture) {
		super(id, name, texture);
	}

	@Override
	public void render(Screen screen, Level level, int tx, int ty, int x, int y) {
		if(level == null) {
			render(screen, CENTER, x, y);
			return;
		}
		boolean u = connectsTo(level.getBg(tx, ty - 1));
		boolean d = connectsTo(level.getBg(tx, ty + 1));
		boolean l = connectsTo(level.getBg(tx - 1, ty));
		boolean r = connectsTo(level.getBg(tx + 1, ty));

		if(u && d && l && !r)
			render(screen, RIGHT_EDGE, x, y);
		else if(u && d && r && !l)
			render(screen, LEFT_EDGE, x, y);
		else if(l && r && u && !d)
			render(screen, BOTTOM_EDGE, x, y);
		else if(l && r && !u && d)
			render(screen, TOP_EDGE, x, y);
		else if(u && r && !d && !l)
			render(screen, BOTTOM_CORNER_LEFT, x, y);
		else if(u && l && !d && !r)
			render(screen, BOTTOM_CORNER_RIGHT, x, y);
		else if(d && l && !u && !r)
			render(screen, TOP_CORNER_RIGHT, x, y);
		else if(d && r && !u && !l)
			render(screen, TOP_CORNER_LEFT, x, y);
		else
			render(screen, CENTER, x, y);
	}
	
	public void render(Screen screen, Rectangle rect, int x, int y) {
		screen.renderSubTexture(texture, rect, x, y);
	}
}
