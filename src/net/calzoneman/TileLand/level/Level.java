package net.calzoneman.TileLand.level;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;

public class Level {
	/** The size, in bytes, of the level header */
	public static final int HEADER_SIZE = 21;
	/** The magic number for the level header */
	public static final int SAVE_MAGIC = 0x54494c45; // "TILE" in ASCII hex
	/** The file format version */
	public static final byte SAVE_VERSION = 0x05;
	static Color lampColor = new Color(180, 180, 80, 100);
	static Color fogColor = new Color(0, 0, 0, 140);
	static Color darkFogColor = new Color(0, 0, 0, 230);
	/** The width of the Level, in tiles */
	private int width = 0;
	/** The height of the Level, in tiles */
	private int height = 0;
	/** The spawnpoint of the Level */
	private Location spawnpoint = new Location(0, 0);
	/** Background layer */
	private BackgroundLayer backgroundLayer;
	/** Foreground layer */
	private ForegroundLayer foregroundLayer;
	private boolean[] visited;
	/** The name of the Level */
	public String name = "save";
	/** Whether or not the Level is initialized */
	public boolean initialized = false;
	
	final Random rand = new Random();
	
	private List<Entity> entities;
	private long tickCount = 0;
	
	/**
	 * Constructor - Generates a new Level with the specified width and height
	 * @param width The width of the new map, in tiles
	 * @param height The height of the new map, in tiles
	 */
	public Level(int width, int height) {
		this.width = width;
		this.height = height;
		this.backgroundLayer = new BackgroundLayer(width, height);
		this.foregroundLayer = new ForegroundLayer(width, height);
		visited = new boolean[width * height];
		entities = new ArrayList<Entity>();
		this.initialized = true;
	}
	
	/**
	 * Constructor - Generates a new Level width the specified width, height, and name
	 * @param width The width of the new map, in tiles
	 * @param height The height of the new map, in tiles
	 * @param name The name of the new map
	 */
	public Level(int width, int height, String name) {
		this(width, height);
		this.name = name;
	}
	
	/**
	 * Constructor - Loads a Level from disk
	 * @param filename The filename to load from, relative to saves/
	 */
	public Level(String filename) {
		this.load(filename);
	}
	
	public Level(Layer background, Layer foreground) {
		this.backgroundLayer = (BackgroundLayer) background;
		this.foregroundLayer = (ForegroundLayer) foreground;
		this.width = background.getWidth();
		this.height = background.getHeight();
		visited = new boolean[width * height];
		this.name = "untitled";
		entities = new ArrayList<Entity>();
		this.initialized = true;
	}

	/**
	 * Saves the map to disk, using the save path saves/[this.name].tl
	 */
	public void save() {
		File savefile = new File("saves/" + name + ".tl");
		try {
			if(!(new File("saves").exists())) new File("saves").mkdir();
			FileOutputStream fos = new FileOutputStream(savefile);
			ByteBuffer buf = ByteBuffer.allocate(HEADER_SIZE + width * height * 6);
			buf.putInt(SAVE_MAGIC);
			buf.put(SAVE_VERSION);
			buf.putInt(width);
			buf.putInt(height);
			buf.putInt(spawnpoint.x);
			buf.putInt(spawnpoint.y);
			short[] bgTiles = backgroundLayer.getTileArray();
			for(int i = 0; i < width * height; i++) {
				buf.putShort(bgTiles[i]);
			}
			short[] fgTiles = foregroundLayer.getTileArray();
			for(int i = 0; i < width * height; i++) {
				buf.putShort(fgTiles[i]);
			}
			byte[] bgData = backgroundLayer.getDataArray();
			buf.put(bgData);
			byte[] fgData = foregroundLayer.getDataArray();
			buf.put(fgData);
			fos.write(buf.array());
			fos.close();
			buf = null;
			System.gc();
		}
		catch(IOException ex) {
			System.out.println("Unable to save level!");
		}
		
	}
	
	/**
	 * Loads a Level from disk and assigns appropriate fields
	 * @param filename The filename to load from, relative to saves/
	 */
	public boolean load(String filename) {
		File savefile = new File("saves/" + filename);
		if(!savefile.exists()) {
			System.out.println("Savefile doesn't exist!");
			return false;
		}
		try {
			FileInputStream fis = new FileInputStream(savefile);
			ByteBuffer hdrbuf = ByteBuffer.allocate(HEADER_SIZE);
			fis.getChannel().read(hdrbuf);
			hdrbuf.rewind();
			if(hdrbuf.getInt() != SAVE_MAGIC) {
				System.out.println("Wrong magic number in level!");
				return false;
			}
			if(hdrbuf.get() != SAVE_VERSION) {
				System.out.println("Wrong save format version");
				return false;
			}
			this.width = hdrbuf.getInt();
			this.height = hdrbuf.getInt();
			this.spawnpoint = new Location(hdrbuf.getInt(), hdrbuf.getInt());
			ByteBuffer databuf = ByteBuffer.allocate(width * height * 6);
			fis.getChannel().read(databuf);
			fis.close();
			databuf.rewind();
			
			ShortBuffer mapBuf = databuf.asShortBuffer();
			short[] bgTiles = new short[width * height];
			short[] fgTiles = new short[width * height];
			mapBuf.get(bgTiles);
			mapBuf.get(fgTiles);
			databuf.position(mapBuf.position() * 2);
			byte[] bgData = new byte[width * height];
			byte[] fgData = new byte[width * height];
			databuf.get(bgData);
			databuf.get(fgData);
			this.backgroundLayer = new BackgroundLayer(width, height, bgTiles, bgData);
			this.foregroundLayer = new ForegroundLayer(width, height, fgTiles, fgData);
			visited = new boolean[width * height];
			entities = new ArrayList<Entity>();
			this.name = filename.substring(0, filename.indexOf(".tl"));
		}
		catch(IOException ex) {
			System.out.println("Unable to load level!");
			return false;
		}
		catch(Exception ex) {
			System.out.println("Unable to load level!");
			return false;
		}
		initialized = true;
		return true;
	}
	
	public void tryFindSpawn(int tries) {
		for(int i = 0; i < tries; i++) {
			int x = rand.nextInt(width);
			int y = rand.nextInt(height);
			if(canPass(x, y)) {
				spawnpoint = new Location(x, y);
				break;
			}
		}
	}
	
	public void render(Screen screen, Player player, int offX, int offY, int maxWidth, int maxHeight) {
		for(int i = offX; i < offX + maxWidth; i++) {
			for(int j = offY; j < offY + maxHeight; j++) {
				Tile bg = getBg(i, j);
				if(bg != null) {
					bg.render(screen, this, i, j, (i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE);
				}
				
				Tile fg = getFg(i, j);
				if(fg != null && fg.id != -1) {
					fg.render(screen, this, i, j, (i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE);
				}
			}
		}
		renderEntities(screen, offX, offY, maxWidth, maxHeight);
	}
	
	public void renderFog(Screen screen, Player player, int offX, int offY, int maxWidth, int maxHeight) {
		int lampRadius = Player.LAMP_RADIUS;
		int px = player.getTilePosition().x;
		int py = player.getTilePosition().y;
		for(int i = offX; i < offX + maxWidth; i++) {
			for(int j = offY; j < offY + maxHeight; j++) {
				if((i - px)*(i - px) + (j - py) * (j - py) <= lampRadius*lampRadius)
					screen.renderFilledRect((i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, lampColor);
				else {
					if(!visited(i, j))
						TileTypes.fog.render(screen, this, i, j, (i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE);
					else
						screen.renderFilledRect((i - offX) * Tile.TILESIZE, (j - offY) * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, fogColor);
				}
			}
		}
	}
	
	
	
	private void renderEntities(Screen screen, int offX, int offY, int maxWidth, int maxHeight) {
		for(Entity ent : entities) {
			Location pos = ent.getPosition();
			Location tpos = ent.getTilePosition();
			if(tpos.x >= offX && tpos.x < offX + maxWidth && tpos.y >= offY && tpos.y < offY + maxHeight) {
				ent.render(screen, this, pos.x - offX * Tile.TILESIZE, pos.y - offY * Tile.TILESIZE);
			}
		}
	}
	
	public void tick() {
		for(Entity ent : entities) {
			ent.think(this, tickCount);
		}
		tickCount++;
	}
	
	public List<Entity> getEntitiesInTile(int tx, int ty) {
		List<Entity> list = new ArrayList<Entity>();
		for(Entity ent : entities) {
			Location pos = ent.getTilePosition();
			if(pos.x == tx && pos.y == ty) {
				list.add(ent);
			}
		}
		return list;
	}
	
	// Tile getters/setters
	
	// Background
	/**
	 * Get the ID of the background tile at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The ID of the background Tile at (x, y), or -1 if the location is invalid
	 */
	public short getBgId(int x, int y) {
		return (short) backgroundLayer.getId(x, y);
	}
	
	/**
	 * Get the Tile in the background layer at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The background Tile at (x, y), or null if the location is invalid
	 */
	public Tile getBg(int x, int y) {
		return backgroundLayer.getTile(x, y);
	}
	
	/**
	 * Set the ID of the background tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param id The ID for the new tile
	 * @return true if the setting was successful, false otherwise
	 */
	public boolean setBgId(int x, int y, int id) {
		return backgroundLayer.setId(x, y, id);
	}
	
	/**
	 * Set the ID of the background tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param t The Tile object to copy the ID from
	 * @return
	 */
	public boolean setBg(int x, int y, Tile t) {
		if(t == null)
			return false;
		return backgroundLayer.setTile(x, y, t);
	}
	
	/**
	 * Gets the data field associated with the given point
	 * @param x The x coordinate to be checked
	 * @param y The y coordinate to be checked
	 * @return The data field associated with the point x, y
	 */
	public int getBgData(int x, int y) {
		return backgroundLayer.getData(x, y);
	}
	
	/**
	 * Sets the data field at x, y
	 * @param x The x coordinate to be set
	 * @param y The y coordinate to be set
	 * @param data The data to be set at x, y
	 * @return True if successful, false if not
	 */
	public boolean setBgData(int x, int y, int data) {
		return backgroundLayer.setData(x, y, data);
	}

	/**
	 * Get the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The ID of the foreground Tile at (x, y), or -1 if the location is invalid
	 */
	public short getFgId(int x, int y) {
		return (short) foregroundLayer.getId(x, y);
	}
	
	/**
	 * Get the Tile in the foreground layer at (x, y)
	 * @param x The x coordinate of the Tile
	 * @param y The y coordinate of the Tile
	 * @return The foreground Tile at (x, y), or null if the location is invalid
	 */
	public Tile getFg(int x, int y) {
		return foregroundLayer.getTile(x, y);
	}
	
	/**
	 * Set the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param id The ID for the new tile
	 * @return true if the setting was successful, false otherwise
	 */
	public boolean setFgId(int x, int y, int id) {
		return foregroundLayer.setId(x, y, id);
	}
	
	/**
	 * Set the ID of the foreground tile at (x, y)
	 * @param x The x coordinate of the tile to set
	 * @param y The y coordinate of the tile to set
	 * @param t The Tile object to copy the ID from
	 * @return
	 */
	public boolean setFg(int x, int y, Tile t) {
		if(t == null)
			return false;
		return foregroundLayer.setTile(x, y, t);
	}
	
	/**
	 * Gets the data field associated with the given point
	 * @param x The x coordinate to be checked
	 * @param y The y coordinate to be checked
	 * @return The data field associated with the point x, y
	 */
	public int getFgData(int x, int y) {
		return foregroundLayer.getData(x, y);
	}
	
	/**
	 * Sets the data field at x, y
	 * @param x The x coordinate to be set
	 * @param y The y coordinate to be set
	 * @param data The data to be set at x, y
	 * @return True if successful, false if not
	 */
	public boolean setFgData(int x, int y, int data) {
		return foregroundLayer.setData(x, y, data);
	}
	
	public void addEntity(Entity ent) {
		entities.add(ent);
	}
	
	public void removeEntity(Entity ent) {
		entities.remove(ent);
	}
	
	public boolean visited(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return false;
		return visited[y * width + x];
	}
	
	public void visit(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return;
		visited[y * width + x] = true;
	}
	
	public boolean canPass(int x, int y) {
		if(x < 0 || y < 0 || x >= width || y >= height)
			return false;
		return !(getFg(x, y).isSolid() || getBg(x, y).isSolid() || getFg(x, y).isLiquid() || getBg(x, y).isLiquid());
	}
	
	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public Location getSpawnpoint() {
		return spawnpoint;
	}

	public void setSpawnpoint(Location spawnpoint) {
		this.spawnpoint = new Location(spawnpoint.x, spawnpoint.y);
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
}
