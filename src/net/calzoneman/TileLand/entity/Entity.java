package net.calzoneman.TileLand.entity;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public abstract class Entity {
	public static int POSITIONS_PER_TILE = 32;
	
	protected Location position;
	
	public Entity(int x, int y) {
		this.position = new Location(x, y);
	}
	
	public Location getTilePosition() {
		return new Location(position.x / POSITIONS_PER_TILE, position.y / POSITIONS_PER_TILE);
	}
	
	public Location getPosition() {
		return new Location(position.x, position.y);
	}
	
	public void setPosition(Location pos) {
		this.position = pos;
	}
	
	public void setPosition(int nx, int ny) {
		setPosition(new Location(nx, ny));
	}
	
	public abstract void render(Screen screen, Level level, int scrX, int scrY);
	
	public abstract void hit(Level level, Mob hitter, int hitDirection);
}
