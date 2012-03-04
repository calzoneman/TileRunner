package net.calzoneman.TileLand.tile;

import org.newdawn.slick.Color;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;

public class Fog extends Tile {
	
	public static final Rectangle TOP_CORNER_LEFT = new Rectangle(0, 0, TILESIZE, TILESIZE);
	public static final Rectangle TOP_EDGE = new Rectangle(TILESIZE, 0, TILESIZE, TILESIZE);
	public static final Rectangle TOP_CORNER_RIGHT = new Rectangle(2*TILESIZE, 0, TILESIZE, TILESIZE);
	public static final Rectangle TOP_CORNER_LEFT_MISSING = new Rectangle(4*TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle TOP_CORNER_RIGHT_MISSING = new Rectangle(3*TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	
	public static final Rectangle LEFT_EDGE = new Rectangle(0, TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle CENTER = new Rectangle(TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle RIGHT_EDGE = new Rectangle(2*TILESIZE, TILESIZE, TILESIZE, TILESIZE);
	
	public static final Rectangle BOTTOM_CORNER_LEFT = new Rectangle(0, 2*TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle BOTTOM_EDGE = new Rectangle(TILESIZE, 2*TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle BOTTOM_CORNER_RIGHT = new Rectangle(2*TILESIZE, 2*TILESIZE, TILESIZE, TILESIZE);
	public static final Rectangle BOTTOM_CORNER_LEFT_MISSING = new Rectangle(4*TILESIZE, 0, TILESIZE, TILESIZE);
	public static final Rectangle BOTTOM_CORNER_RIGHT_MISSING = new Rectangle(3*TILESIZE, 0, TILESIZE, TILESIZE);
	
	static Color lightFog = new Color(0, 0, 0, 140);
	
	public Fog(short id, String name, Texture texture) {
		super(id, name, texture);
	}

	@Override
	public void render(Screen screen, Level level, int tx, int ty, int x, int y) {
		if(level == null) {
			render(screen, CENTER, x, y);
			return;
		}
		boolean u = !level.visited(tx, ty - 1);
		boolean d = !level.visited(tx, ty + 1);
		boolean l = !level.visited(tx - 1, ty);
		boolean r = !level.visited(tx + 1, ty);

		boolean ul = u && l && level.visited(tx - 1, ty - 1);
		boolean ur = u && r && level.visited(tx + 1, ty - 1);
		boolean dl = d && l && level.visited(tx - 1, ty + 1);
		boolean dr = d && r && level.visited(tx + 1, ty + 1);
		
		if(ul && !ur && !dl && !dr)
			render(screen, TOP_CORNER_LEFT_MISSING, x, y);
		else if(ur && !ul && !dl && !dr)
			render(screen, TOP_CORNER_RIGHT_MISSING, x, y);
		else if(dl && !ul && !ur && !dr)
			render(screen, BOTTOM_CORNER_LEFT_MISSING, x, y);
		else if(dr && !ul && !ur && !dl)
			render(screen, BOTTOM_CORNER_RIGHT_MISSING, x, y);
		else {
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
			else {
				render(screen, CENTER, x, y);
			}
		}
	}
	
	public void render(Screen screen, Rectangle rect, int x, int y) {
		if(!rect.equals(CENTER))
			screen.renderFilledRect(x, y, TILESIZE, TILESIZE, lightFog);
		screen.renderSubTexture(texture, rect, x, y);
	}
}