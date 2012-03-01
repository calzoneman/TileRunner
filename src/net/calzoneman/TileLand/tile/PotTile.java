package net.calzoneman.TileLand.tile;

import java.io.IOException;

import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.entity.TestEntity;
import net.calzoneman.TileLand.gfx.Sprite;
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
			try {
				level.addEntity(new TestEntity(level, ex, ey, mag, ang, new Sprite(TileTypes.spritesheet.getTileTexture(1 + 10 * TileTypes.spritesheet.width))));
				level.setFgId(tx, ty, TypeId.AIR);
			} 
			catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
