package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;

public class DirectionalFgTile extends DirectionalTile {
	
	public DirectionalFgTile(short id, String name, Texture texture) {
		super(id, name, texture);
		foreground = true;
	}

	@Override
	public void render(Screen screen, Level level, int tx, int ty, int x, int y) {
		if(level == null) {
			render(screen, CENTER, x, y);
			return;
		}
		boolean u = connectsTo(level.getFg(tx, ty - 1));
		boolean d = connectsTo(level.getFg(tx, ty + 1));
		boolean l = connectsTo(level.getFg(tx - 1, ty));
		boolean r = connectsTo(level.getFg(tx + 1, ty));

		boolean ul = u && l && transitionsTo(level.getFg(tx - 1, ty - 1));
		boolean ur = u && r && transitionsTo(level.getFg(tx + 1, ty - 1));
		boolean dl = d && l && transitionsTo(level.getFg(tx - 1, ty + 1));
		boolean dr = d && r && transitionsTo(level.getFg(tx + 1, ty + 1));
		
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
			else
				render(screen, CENTER, x, y);
		}
	}
}
