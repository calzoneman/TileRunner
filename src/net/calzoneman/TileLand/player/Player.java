package net.calzoneman.TileLand.player;


import java.util.Random;

import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.gfx.PlayerSprite;
import net.calzoneman.TileLand.gfx.Renderer;
import net.calzoneman.TileLand.gfx.TilelandFont;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.Inventory;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.PlayerInventory;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.tile.Tile;

public class Player {
	
	public static final int POSITION_FACTOR = 0;
	public static final int LAMP_RADIUS = 4;
	
	/** The name of the Player */
	private String name;
	/** The player sprite */
	private PlayerSprite sprite;
	/** The Level in which the Player currently exists */
	private Level level;
	/** The Player's position within the level */
	private Location position;
	/** The current direction the player is facing */
	private int facing = PlayerSprite.FACING_DOWN;
	private PlayerInventory inventory;
	private final Random rand = new Random();
	
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
		this.setSprite(sprite);
		this.setLevel(level);
		this.setName(name);
		this.setPosition(position);
		this.inventory = new PlayerInventory();
	}
	
	public boolean move(int currentMoveKey) {
		String move = "";
		switch(currentMoveKey) {
			case Keyboard.KEY_UP:
				if(!level.canPass(position.x >> POSITION_FACTOR, (position.y-1) >> POSITION_FACTOR))
					break;
				move = "up";
				break;
			case Keyboard.KEY_DOWN:
				if(!level.canPass(position.x >> POSITION_FACTOR, (position.y+1) >> POSITION_FACTOR))
					break;
				move = "down";
				break;
			case Keyboard.KEY_LEFT:
				if(!level.canPass((position.x-1) >> POSITION_FACTOR, position.y >> POSITION_FACTOR))
					break;
				move = "left";
				break;
			case Keyboard.KEY_RIGHT:
				if(!level.canPass((position.x+1) >> POSITION_FACTOR, position.y >> POSITION_FACTOR))
					break;
				move = "right";
				break;
			default:
				break;
		}
		if(!move.equals("")) {
			int oldFacing = this.facing;
			if(move.equals("up")) {
				position.y--;
				setFacing(PlayerSprite.FACING_UP);
			}
			else if(move.equals("right")) {
				position.x++;
				setFacing(PlayerSprite.FACING_RIGHT);
			}
			else if(move.equals("down")) {
				position.y++;
				setFacing(PlayerSprite.FACING_DOWN);
			}
			else if(move.equals("left")) {
				position.x--;
				setFacing(PlayerSprite.FACING_LEFT);
			}
			if(oldFacing == facing)
				sprite.nextFrame();
			int px = getTilePosition().x;
			int py = getTilePosition().y;
			for(int i = px - LAMP_RADIUS; i <= px + LAMP_RADIUS; i++) {
				for(int j = py - LAMP_RADIUS; j <= py + LAMP_RADIUS; j++) {
					if((i - px) * (i - px) + (j - py) * (j - py) <= LAMP_RADIUS + rand.nextInt(2) - 1)
						level.visit(i, j);
				}
			}
			return true;
		}
		else {
			sprite.resetFrame();
			return false;
		}
	}

	public PlayerSprite getSprite() {
		return sprite;
	}

	public void setSprite(Texture sprite) {
		this.sprite = new PlayerSprite(sprite);
	}

	public Level getLevel() {
		return level;
	}

	public void setLevel(Level level) {
		this.level = level;
	}

	public Location getPosition() {
		return position;
	}
	
	public Location getTilePosition() {
		return new Location(position.x >> POSITION_FACTOR, position.y >> POSITION_FACTOR);
	}

	public void setPosition(Location position) {
		this.position = new Location(position); // Create a new instance of Location so we aren't modifying the original input
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		if(name.equals("calzoneman"))
			name = TilelandFont.COLOR_CODE_DELIMITER + "9calzoneman";
		this.name = name;
	}
	
	public Item getHeldItem() {
		ItemStack it = inventory.getQuickbar().getSelectedItemStack();
		if(it == null)
			return null;
		return it.getItem();
	}
	
	public PlayerInventory getPlayerInventory() {
		return this.inventory;
	}
	
	public Inventory getInventory() {
		return this.inventory;
	}

	public void render(int x, int y) {
		// Draw the player sprite
		this.sprite.render(x, y - (PlayerSprite.PLAYER_HEIGHT - Tile.TILESIZE));
		int w = Renderer.getFont().getWidth(name);
		int sx = x + PlayerSprite.PLAYER_WIDTH / 2 - w/2;
		int sy = y - PlayerSprite.PLAYER_HEIGHT;
		Renderer.renderString(sx, sy, name, Color.black);
	}

	public int getFacing() {
		return facing;
	}

	public void setFacing(int facing) {
		this.facing = facing;
		this.sprite.setFacing(facing);
	}

}
