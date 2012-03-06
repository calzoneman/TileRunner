package net.calzoneman.TileLand.tile;


import net.calzoneman.TileLand.entity.CoinEntity;
import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;

import org.newdawn.slick.opengl.Texture;

public class PotTile extends Tile {

	public PotTile(short id, String name, Texture texture) {
		super(id, name, texture);
		solid = true;
		foreground = true;
	}
	
	@Override
	public void hit(Level level, Player player, int tx, int ty) {
		int ex = tx * Entity.POSITIONS_PER_TILE + Entity.POSITIONS_PER_TILE / 2;
		int ey = ty * Entity.POSITIONS_PER_TILE + Entity.POSITIONS_PER_TILE / 2;

		int count = 1 + rand.nextInt(3);
		for(int i = 0; i < count; i++) {
			int mag = rand.nextInt(4) + 4;
			double ang = rand.nextDouble() * 2 * Math.PI;
			int type = rand.nextInt() < 5 ? CoinEntity.TYPE_SILVER : CoinEntity.TYPE_COPPER;
			level.addEntity(new CoinEntity(level, ex, ey, mag, ang, type));
			level.setFgId(tx, ty, TypeId.BROKEN_POT);
		}
	}
}
