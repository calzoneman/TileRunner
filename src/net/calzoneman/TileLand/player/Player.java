package net.calzoneman.TileLand.player;



import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.entity.Mob;
import net.calzoneman.TileLand.gfx.MobSprite;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.PlayerInventory;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;

public class Player extends Mob {
	
	public static final int LAMP_RADIUS = 4;
	
	/** The name of the Player */
	private String name;
	private PlayerInventory inventory;
	
	/**
	 * Parameterless constructor
	 */
	public Player() {
		this(null, null, "unnamed player", new Location(0, 0));
	}
	
	public Player(String name) {
		this(null, null, name, new Location(0, 0));
	}
	
	/**
	 * Constructor which sets the level and sets the position to the spawnpoint of the level
	 * @param level The level to place the Player into
	 */
	public Player(Level level) {
		this(null, level, "unnamed player", level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, and level, and sets the position to the spawnpoint of the level
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 */
	public Player(Texture sprite, Level level) {
		this(sprite, level, "unnamed player", level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, name, and level, and sets the position to the spawnpoint of the level
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 */
	public Player(Texture sprite, Level level, String name) {
		this(sprite, level, name, level.getSpawnpoint());
	}
	
	/**
	 * Constructor which sets the sprite, level, name, and position
	 * @param sprite The Texture of the Player
	 * @param level The Level to place the player into
	 * @param name The name of the Player
	 * @param position The position of the Player
	 */
	public Player(Texture sprite, Level level, String name, Location position) {
		super(position.x, position.y, new MobSprite(sprite));
		this.setLevel(level);
		this.setName(name);
		this.setPosition(position);
		this.inventory = new PlayerInventory();
	}
	
	@Override
	public boolean move(int direction) {
		boolean success = super.move(direction);
		if(success) {
			int px = getTilePosition().x;
			int py = getTilePosition().y;
			for(int i = px - LAMP_RADIUS; i <= px + LAMP_RADIUS; i++) {
				for(int j = py - LAMP_RADIUS; j <= py + LAMP_RADIUS; j++) {
					if((i - px) * (i - px) + (j - py) * (j - py) <= LAMP_RADIUS + rand.nextInt(2) - 1)
						level.visit(i, j);
				}
			}
		}
		return success;
	}
	
	@Override
	public boolean move(int direction, boolean sprint) {
		boolean success = super.move(direction, sprint);
		if(success) {
			int px = getTilePosition().x;
			int py = getTilePosition().y;
			for(int i = px - LAMP_RADIUS; i <= px + LAMP_RADIUS; i++) {
				for(int j = py - LAMP_RADIUS; j <= py + LAMP_RADIUS; j++) {
					if((i - px) * (i - px) + (j - py) * (j - py) <= LAMP_RADIUS + rand.nextInt(2) - 1)
						level.visit(i, j);
				}
			}
		}
		return success;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.equals("calzoneman"))
			name = Font.COLOR_CODE_DELIMITER + "9calzoneman";
		this.name = name;
	}
	
	public Item getHeldItem() {
		ItemStack it = inventory.getQuickbar().getSelectedItemStack();
		if(it == null)
			return null;
		return it.getItem();
	}
	
	public PlayerInventory getInventory() {
		return this.inventory;
	}

	@Override
	public void render(Screen screen, Level level, int x, int y) {
		super.render(screen, level, x, y);
		int w = Font.getWidth(name);
		int sx = x - w/2;
		int sy = y - sprite.getHeight();
		Font.draw(name, screen, sx, sy, Color.black);
	}
	
	@Override
	public void hit(Level level, Mob hitter, int hitDirection) {
	
	}

}
