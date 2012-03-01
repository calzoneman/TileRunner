package net.calzoneman.TileLand.entity;

import org.lwjgl.input.Keyboard;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gfx.Sprite;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public abstract class Entity {
	public static int POSITIONS_PER_TILE = 32;
	
	protected Level level;
	protected Location position;
	protected Sprite sprite;
	
	public Entity(Level level, int x, int y, Sprite sprite) {
		this.level = level;
		this.position = new Location(x, y);
		this.sprite = sprite;
	}
	
	public boolean move(int direction) {
		return move(direction, false);
	}
	
	public boolean move(int direction, boolean sprint) {
		boolean moved = false;
		int amt = 1;
		switch(direction) {
			case Keyboard.KEY_UP:
				if(checkCollision(position.x, position.y-amt))
					break;
				position.y -= amt;
				moved = true;
				break;
			case Keyboard.KEY_DOWN:
				if(checkCollision(position.x, position.y+amt))
					break;
				position.y += amt;
				moved = true;
				break;
			case Keyboard.KEY_LEFT:
				if(checkCollision(position.x-amt, position.y))
					break;
				position.x -= amt;
				moved = true;
				break;
			case Keyboard.KEY_RIGHT:
				if(checkCollision(position.x+amt, position.y))
					break;
				position.x += amt;
				moved = true;
				break;
			default:
				break;
		}
		return moved;
	}
	
	public boolean checkCollision(int x, int y) {
		Location[] p = new Location[6];
		p[0] = new Location(
				x / POSITIONS_PER_TILE,
				y / POSITIONS_PER_TILE);
		p[1] = new Location(
				(x - sprite.getWidth() / 2 + 2) / POSITIONS_PER_TILE,
				y / POSITIONS_PER_TILE);
		p[2] = new Location(
				(x + sprite.getWidth() / 2 - 2) / POSITIONS_PER_TILE,
				y / POSITIONS_PER_TILE);
		p[3] = new Location(
				x / POSITIONS_PER_TILE,
				(y + sprite.getHeight() / 2) / POSITIONS_PER_TILE);
		p[4] = new Location(
				(x + sprite.getWidth() / 2 - 2) / POSITIONS_PER_TILE,
				(y + sprite.getHeight() / 2) / POSITIONS_PER_TILE);
		p[5] = new Location(
				(x - sprite.getWidth() / 2 + 2) / POSITIONS_PER_TILE,
				(y + sprite.getHeight() / 2) / POSITIONS_PER_TILE);
		for(Location pos : p) {
			if(!level.canPass(pos.x, pos.y)) {
				return true;
			}
		}
		return false;
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
	
	public void render(Screen screen, Level level, int scrX, int scrY) {
		sprite.render(screen, scrX - sprite.getWidth() / 2, scrY - sprite.getHeight() / 2);
	}
	
	public abstract void think(Level level, long ticks);
	
	public abstract void hit(Level level, Mob hitter, int hitDirection);
}
