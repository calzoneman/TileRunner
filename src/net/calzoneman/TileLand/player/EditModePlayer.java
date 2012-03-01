package net.calzoneman.TileLand.player;



import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.gfx.MobSprite;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public class EditModePlayer extends Player {
	
	public EditModePlayer() {
		super();
	}
	
	public EditModePlayer(String name) {
		super(name);
	}
	
	public EditModePlayer(Level level) {
		super(level);
	}
	
	public EditModePlayer(Texture sprite, Level level) {
		super(sprite, level);
	}

	public EditModePlayer(Texture sprite, Level level, String name) {
		super(sprite, level, name);
	}

	public EditModePlayer(Texture sprite, Level level, String name, Location position) {
		super(sprite, level, name, position);
	}
	
	@Override
	public boolean move(int direction) {
		return move(direction, false);
	}
	
	@Override
	public boolean move(int direction, boolean sprint) {
		boolean moved = false;
		int oldFacing = this.facing;
		int amt = sprint ? sprintSpeed : walkSpeed;
		switch(direction) {
			case Keyboard.KEY_UP:
				setFacing(MobSprite.FACING_UP);
				position.y -= amt;
				moved = true;
				break;
			case Keyboard.KEY_DOWN:
				setFacing(MobSprite.FACING_DOWN);
				position.y += amt;
				moved = true;
				break;
			case Keyboard.KEY_LEFT:
				setFacing(MobSprite.FACING_LEFT);
				position.x -= amt;
				moved = true;
				break;
			case Keyboard.KEY_RIGHT:
				setFacing(MobSprite.FACING_RIGHT);
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
}
