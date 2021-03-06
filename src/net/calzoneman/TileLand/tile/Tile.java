package net.calzoneman.TileLand.tile;

import java.util.Random;

import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.entity.Mob;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;

public class Tile {

	public static final int TILESIZE = 32;

	static final Color hoverGreen = new Color(0, 255, 0, 120);
	static final Color solidGreen = new Color(0, 255, 0);
	static final Color hoverRed = new Color(255, 0, 0, 120);
	static final Color solidRed = new Color(255, 0, 0);

	public final short id;
	public final String name;
	protected Texture texture;
	
	protected final Random rand = new Random();

	protected boolean solid = false;
	protected boolean liquid = false;
	protected boolean foreground = false;

	public Tile(short id, String name) {
		this.id = id;
		this.name = name;
	}

	public Tile(short id, String name, Texture texture) {
		this.id = id;
		this.name = name;
		this.texture = texture;
	}

	public Texture getTexture() {
		return texture;
	}

	public void setTexture(Texture tex) {
		texture = tex;
	}

	public boolean connectsTo(Tile other) {
		return false;
	}
	
	public boolean transitionsTo(Tile other) {
		return false;
	}

	public void hit(Level level, Player player, int tx, int ty) {
		
	}
	
	public void collide(Level level, Mob mob, int tx, int ty) {
		
	}

	public void render(Screen screen, Level level, int tx, int ty, int x, int y) {
		screen.renderTexture(texture, x, y);
	}

	public boolean isSolid() {
		return solid;
	}
	
	public boolean isLiquid() {
		return liquid;
	}
	
	public boolean isForeground() {
		return foreground;
	}
}
