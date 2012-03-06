package net.calzoneman.TileLand.entity;

import java.io.IOException;

import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.AnimatedSprite;
import net.calzoneman.TileLand.level.Level;

public class CoinEntity extends ItemEntity {
	
	public static final int TYPE_COPPER = 0;
	public static final int TYPE_SILVER = 1;
	public static final int TYPE_GOLD = 2;
	
	static final int SPRITE_DELAY = 100;
	
	protected int coinType;

	public CoinEntity(Level level, int x, int y, int vel, double ang, int type) {
		super(level, x, y, vel, ang, null);
		//super(level, x, y, vel, ang, new AnimatedSprite(ResourceManager.ITEMSHEET.getCustomTileTexture(0, 0, 4, 1), 32, SPRITE_DELAY));
		Texture tex = null;
		try {
			if(type == TYPE_COPPER) {
				tex = ResourceManager.ITEMSHEET.getCustomTileTexture(0, 0, 4, 1);
			}
			else if(type == TYPE_SILVER) {
				tex = ResourceManager.ITEMSHEET.getCustomTileTexture(0, 1, 4, 1);
			}
			else if(type == TYPE_GOLD) {
				tex = ResourceManager.ITEMSHEET.getCustomTileTexture(0, 2, 4, 1);
			}
		}
		catch(IOException e) {
			e.printStackTrace();
		}
		
		this.sprite = new AnimatedSprite(tex, 32, SPRITE_DELAY);
		this.coinType = type;
	}
	
	public int value() {
		switch(coinType) {
			case TYPE_COPPER:
				return 1;
			case TYPE_SILVER:
				return 100;
			case TYPE_GOLD:
				return 10000;
			default:
				return 0;
		}
	}

}
