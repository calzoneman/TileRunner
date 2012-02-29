package net.calzoneman.TileLand.tile;

import org.newdawn.slick.opengl.Texture;

public class PotTile extends Tile {

	public PotTile(short id, String name, Texture texture) {
		super(id, name, texture);
		solid = true;
	}
}
