package net.calzoneman.TileLand.entity;

import java.io.IOException;

import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.AnimatedSprite;
import net.calzoneman.TileLand.level.Level;

public class CoinEntity extends ItemEntity {
	
	static final int SPRITE_DELAY = 100;

	public CoinEntity(Level level, int x, int y, int vel, double ang) throws IOException {
		super(level, x, y, vel, ang, new AnimatedSprite(ResourceManager.ITEMSHEET.getCustomTileTexture(0, 0, 4, 1), 32, SPRITE_DELAY));
	}

}
