package net.calzoneman.TileLand.tile;

import java.util.HashMap;

import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.opengl.Texture;

import net.calzoneman.TileLand.TileLand;

public class TileTypes {
	/** A Dictionary of Background Tiles keyed by id */
	private static HashMap<Integer, Tile> bgTileTypes;
	/** A Dictionary of Foreground Tiles keyed by id */
	private static HashMap<Integer, Tile> fgTileTypes;
	/** A Dictionary of Background Tile IDs keyed by name */
	private static HashMap<String, Integer> bgTileNames;
	/** A Dictionary of Foreground Tile IDs keyed by name */
	private static HashMap<String, Integer> fgTileNames;
	
	public static void init() {		
		// Initialize Tile type Dictionaries
		bgTileTypes = new HashMap<Integer, Tile>();
		fgTileTypes = new HashMap<Integer, Tile>();
		
		// Initialize Tile name Dictionaries
		fgTileNames = new HashMap<String, Integer>();
		bgTileNames = new HashMap<String, Integer>();
		
		Texture tex = TileLand.getTextureManager().getTexture("tiles.png");
		
		// Initialize Tile types
		addBgTile(new SolidTile(-1, "null", tex, new Rectangle(0, 0, 32, 32)));
		addBgTile(new Tile(0, "grass1", tex, new Rectangle(160, 192, 32, 32)));
		addBgTile(new Tile(1, "grass2", tex, new Rectangle(192, 192, 32,32)));
		addBgTile(new Tile(2, "grass3", tex, new Rectangle(224, 192, 32, 32)));
		addBgTile(new MultidirectionalTile(3, "lake", tex, new Rectangle(160, 288, 96, 96), false));
		addBgTile(new MultidirectionalTile(4, "sand", tex, new Rectangle(320, 288, 96, 96), false));
		addBgTile(new MultidirectionalTile(5, "cobbleroad", tex, new Rectangle(0, 192, 96, 96), false));
		addBgTile(new MultidirectionalTile(6, "snow", tex, new Rectangle(0, 384, 96, 96), false));
		addBgTile(new MultidirectionalTile(7, "frozenlake", tex, new Rectangle(0, 288, 96, 96), false));
		addBgTile(new MultidirectionalTile(8, "dirt", tex, new Rectangle(352, 0, 96, 96), false));
		
		addFgTile(new Tile(0, "air", null, null, TileProperties.FOREGROUND));
		addFgTile(new SolidTile(1, "tree1", tex, new Rectangle(32, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(2, "tree2", tex, new Rectangle(64, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(3, "bush1", tex, new Rectangle(96, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(4, "sign1", tex, new Rectangle(128, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(5, "sign2", tex, new Rectangle(160, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(6, "sign3", tex, new Rectangle(192, 128, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new SolidTile(7, "rock1", tex, new Rectangle(0, 160, 32, 32), TileProperties.FOREGROUND));
		addFgTile(new MultidirectionalTile(8, "mountain", tex, new Rectangle(320, 192, 96, 96), true, TileProperties.FOREGROUND | TileProperties.SOLID));
	}
	
	/**
	 * Insert a background Tile into the appropriate dictionaries
	 * @param bg The Tile to be inserted
	 */
	private static void addBgTile(Tile bg) {
		bgTileTypes.put(bg.getId(), bg);
		bgTileNames.put(bg.getName(), bg.getId());
	}
	
	/**
	 * Insert a foreground Tile into the appropriate dictionaries
	 * @param fg The Tile to be inserted
	 */
	private static void addFgTile(Tile fg) {
		fgTileTypes.put(fg.getId(), fg);
		fgTileNames.put(fg.getName(), fg.getId());
	}
	
	/**
	 * Retrieves the default background tiletype
	 * @return The default background tiletype
	 */
	public static Tile getDefaultBg() {
		return bgTileTypes.get(8);
	}
	
	/**
	 * Retrieves the default foreground tiletype
	 * @return The default foreground tiletype
	 */
	public static Tile getDefaultFg() {
		return fgTileTypes.get(0);
	}
	
	/**
	 * Retrieves the background Tile with the given id
	 * @param id The id of the Tile to be retrieved
	 * @return The background Tile with the requested id, or null if such a Tile does not exist
	 */
	public static Tile getBgTile(int id) {
		return bgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the background Tile with the given name
	 * @param name The name of the Tile to be retrieved
	 * @return The background Tile with the requested name, or null if such a Tile does not exist
	 */
	public static Tile getBgTile(String name) {
		Integer id = bgTileNames.get(name);
		if(id == null)
			return null;
		return bgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the foreground Tile with the given id
	 * @param id The id of the Tile to be retrieved
	 * @return The foreground Tile with the requested id, or null if such a Tile does not exist
	 */
	public static Tile getFgTile(int id) {
		return fgTileTypes.get(id);
	}
	
	/**
	 * Retrieves the foreground Tile with the given name
	 * @param name The name of the Tile to be retrieved
	 * @return The foreground Tile with the requested name, or null if such a Tile does not exist
	 */
	public static Tile getFgTile(String name) {
		Integer id = fgTileNames.get(name);
		if(id == null)
			return null;
		return fgTileTypes.get(id);
	}
	
	public static boolean playerBreakableBg(int bg) {
		switch(bg) {
			case -1:
			case 3:
			case 7:
			case 8:
				return false;
			default:
				return true;
		}
	}
	
	public static boolean playerBreakableFg(int fg) {
		switch(fg) {
			case 0:
				return false;
			default:
				return true;
		}
	}
}
