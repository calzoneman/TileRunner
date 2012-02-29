package net.calzoneman.TileLand.entity;

import java.util.Random;

import org.lwjgl.input.Keyboard;

import net.calzoneman.TileLand.gfx.MobSprite;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public abstract class Mob extends Entity {
	
	protected MobSprite sprite;
	protected int facing = MobSprite.FACING_DOWN;
	protected int movedSinceSpriteUpdate = 0;
	protected Level level;
	protected final Random rand = new Random();


	public Mob(int x, int y, MobSprite sprite) {
		super(x, y);
		this.sprite = sprite;
	}
	
	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}
	
	public MobSprite getSprite() {
		return sprite;
	}
	
	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
		this.sprite.setFacing(facing);
	}
	
	public boolean move(int direction) {
		return move(direction, false);
	}
	
	public boolean move(int direction, boolean sprint) {
		boolean moved = false;
		int oldFacing = this.facing;
		int amt = sprint ? 4 : 2;
		switch(direction) {
			case Keyboard.KEY_UP:
				setFacing(MobSprite.FACING_UP);
				if(checkCollision(position.x, position.y-amt))
					break;
				position.y -= amt;
				moved = true;
				break;
			case Keyboard.KEY_DOWN:
				setFacing(MobSprite.FACING_DOWN);
				if(checkCollision(position.x, position.y+amt))
					break;
				position.y += amt;
				moved = true;
				break;
			case Keyboard.KEY_LEFT:
				setFacing(MobSprite.FACING_LEFT);
				if(checkCollision(position.x-amt, position.y))
					break;
				position.x -= amt;
				moved = true;
				break;
			case Keyboard.KEY_RIGHT:
				setFacing(MobSprite.FACING_RIGHT);
				if(checkCollision(position.x+amt, position.y))
					break;
				position.x += amt;
				moved = true;
				break;
			default:
				break;
		}
		if(moved)
			movedSinceSpriteUpdate += amt;
		if(!moved)
			sprite.resetFrame();
		if(oldFacing == facing && movedSinceSpriteUpdate >= POSITIONS_PER_TILE / 2) {
			sprite.nextFrame();	
			movedSinceSpriteUpdate = 0;
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
	

	@Override
	public void render(Screen screen, Level level, int scrX, int scrY) {
		int xo = scrX - sprite.getWidth() / 2;
		int yo = scrY - sprite.getHeight() / 2;
		
		sprite.render(screen, xo, yo);
	}
	
	public static class MoveDirection {
		public static final int UP = Keyboard.KEY_UP;
		public static final int DOWN = Keyboard.KEY_DOWN;
		public static final int LEFT = Keyboard.KEY_LEFT;
		public static final int RIGHT = Keyboard.KEY_RIGHT;
	}
}
