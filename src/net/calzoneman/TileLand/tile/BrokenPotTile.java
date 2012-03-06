package net.calzoneman.TileLand.tile;


import org.newdawn.slick.opengl.Texture;

public class BrokenPotTile extends Tile {

	public BrokenPotTile(short id, String name, Texture texture) {
		super(id, name, texture);
		foreground = true;
	}
}
