package net.calzoneman.TileLand.inventory;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.tile.MultitextureTile;
import net.calzoneman.TileLand.tile.Tile;

public class TileItem extends Item {
	
	protected Tile tile;

	public TileItem(Tile tile, int data) {
		super(tile.id, tile.name, null, data);
		this.tile = tile;
	}
	
	public Tile getTile() {
		return tile;
	}
	
	@Override
	public void render(Screen screen, int x, int y) {
		if(tile instanceof MultitextureTile)
			((MultitextureTile) tile).render(screen, x, y, data);
		else
			tile.render(screen, null, 0, 0, x, y);
	}

}
