package net.calzoneman.TileLand.entity;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gfx.Sprite;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public class ItemEntity extends Entity {
	
	protected int velocity;
	protected double ang;

	public ItemEntity(Level level, int x, int y, int vel, double ang, Sprite sprite) {
		super(level, x, y, sprite);
		this.velocity = vel;
		this.ang = ang;
	}

	@Override
	public void render(Screen screen, Level level, int scrX, int scrY) {
		super.render(screen, level, scrX, scrY);
	}

	@Override
	public void hit(Level level, Mob hitter, int hitDirection) {

	}

	@Override
	public void think(Level level, long ticks) {
		//if((ticks % 10) != 0)
		//	return;
		Location oldPosition = new Location(position);
		if(Math.abs(velocity) > 0) {
			position.x += velocity * Math.cos(ang);
			position.y += velocity * Math.sin(ang);
			
			if(checkCollision(position.x, position.y))
				position = oldPosition;
			
			velocity += (velocity < 0) ? 1 : -1;
		}
	}

}
