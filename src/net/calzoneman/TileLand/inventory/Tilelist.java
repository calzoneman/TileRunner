package net.calzoneman.TileLand.inventory;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;
import net.calzoneman.TileLand.tile.TypeId;

public class Tilelist {
	static final int SLOT_PADDING = 3;
	private int x;
	private int y;
	private int rows;
	private int cols;
	private int width;
	private int height;
	private ItemStack[] contents;
	private int selected = 0;
	private Color slotBgColor = new Color(0.2f, 0.2f, 0.2f, 0.5f);
	private Color barBgColor = new Color(0.4f, 0.4f, 0.4f, 0.5f);
	
	public Tilelist() {
		rows = 2;
		cols = 16;
		x = 10;
		y = 10;
		width = cols * Tile.TILESIZE + (cols + 1) * SLOT_PADDING;
		height = rows * Tile.TILESIZE + (rows + 1) * SLOT_PADDING;
		
		populate();
	}
	
	private void populate() {
		this.contents = new ItemStack[rows * cols];
		
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.GRASS))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.GRASS), 1)) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.GRASS), 2)) );

		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.LAKE))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.SAND))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.COBBLESTONE_ROAD))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.SNOWY_GRASS))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.LAKE_FROZEN))) );
		
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.TREE))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.TREE), 1)) );

		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.BUSH))) );
		
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.SIGN))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.SIGN), 1)) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.SIGN), 2)) );

		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.ROCK))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.MOUNTAIN))) );
		addItemStack( new ItemStack(new TileItem(TileTypes.getTile(TypeId.POT))) );
	}
	
	private boolean addItemStack(ItemStack it) {
		for(int i = 0; i < contents.length; i++) {
			if(contents[i] == null) {
				contents[i] = it;
				return true;
			}
		}
		return false;
	}
	
	public void setPosition(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public int getX() { return x; }
	
	public int getY() { return y; }
	
	public void setSize(int w, int h) {
		this.width = w;
		this.height = h;
	}
	
	public int getWidth() { return width; }
	
	public int getHeight() { return height; }
	
	public void nextSlot() {
		selected++;
		if(selected >= rows * cols)
			selected = 0;
	}
	
	public void prevSlot() {
		selected--;
		if(selected < 0)
			selected = rows * cols - 1;
	}
	
	public boolean setSelectedSlot(int selected) {
		if(selected < 0 || selected >= rows * cols)
			return false;
		this.selected = selected;
		return true;
	}
	
	public int getSelectedSlot() {
		return this.selected;
	}
	
	public ItemStack getSelectedItemStack() {
		return getItemStack(getSelectedSlot());
	}
	
	public boolean setItemStack(int slot, ItemStack obj) {
		if(slot < 0 || slot >= rows * cols)
			return false;
		contents[slot] = obj;
		return true;
	}
	
	public ItemStack getItemStack(int slot) {
		if(slot < 0 || slot >= rows * cols)
			return null;
		return contents[slot];
	}
	
	public int getCount() {
		return rows * cols;
	}

	public void render(Screen screen) {
		screen.renderFilledRect(this.x, this.y, this.width, this.height, barBgColor);
		for(int i = 0; i < rows; i++) {
			for(int j = 0; j < cols; j++) {
				int xx = this.x + SLOT_PADDING*(j+1) + j*Tile.TILESIZE;
				int yy = this.y + SLOT_PADDING*(i+1) + i*Tile.TILESIZE;
				// Draw the slot background
				screen.renderFilledRect(xx, yy, Tile.TILESIZE, Tile.TILESIZE, slotBgColor);
				// Draw the contents (where applicable)
				if(contents[i * cols + j] != null)
					contents[i * cols + j].render(screen, xx, yy);
				// Draw the selection border
				if((i * cols + j) == selected)
					screen.renderRect(xx, yy, Tile.TILESIZE, Tile.TILESIZE, Color.green);
			}
		}
	}
}
