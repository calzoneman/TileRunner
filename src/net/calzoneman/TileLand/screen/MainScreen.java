package net.calzoneman.TileLand.screen;


import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.TileItem;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TypeId;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class MainScreen extends GameScreen {
	
	private int currentMoveKey;
	private long lastMoveTime;
	
	static final long CLICK_DELAY = 50000000L;
	
	/** Colors for Mouse overlay */
	private static Color transparentRed = new Color(255, 0, 0, 130);
	private static Color transparentGreen = new Color(0, 255, 0, 130);
	static Color lampColor = new Color(180, 180, 80, 100);
	static Color fogColor = new Color(0, 0, 0, 140);
	static Color darkFogColor = new Color(0, 0, 0, 230);
	
	public MainScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
		this.currentMoveKey = -1;
		this.lastMoveTime = 0;
	}
	
	@Override
	public void handleInput() {
		handleMouse();
		handleKeyboard();
	}

	public void handleMouse() {
		Player ply = parent.getPlayer();
		while(Mouse.next()) {
			// Update mouse button state
			if(Mouse.getEventButton() != -1)
				mouse[Mouse.getEventButton()] = Mouse.getEventButtonState();
			click(ply, Mouse.getEventX(), Mouse.getEventY());
		}
	}

	public void handleKeyboard() {
		Player ply = parent.getPlayer();
		while(Keyboard.next()) {
			if(Keyboard.getEventKey() != -1) {
				// Update the key state
				keys[Keyboard.getEventKey()] = Keyboard.getEventKeyState();
				// Update the current movement key if applicable
				if(Keyboard.getEventKeyState()) {
					switch(Keyboard.getEventKey()) {
						case Keyboard.KEY_UP:
						case Keyboard.KEY_DOWN:
						case Keyboard.KEY_LEFT:
						case Keyboard.KEY_RIGHT:
							currentMoveKey = Keyboard.getEventKey();
							break;
						default:
							break;
					}
				}
			}
			if(!keys[Keyboard.KEY_UP] && !keys[Keyboard.KEY_DOWN] && !keys[Keyboard.KEY_LEFT] && !keys[Keyboard.KEY_RIGHT])
				currentMoveKey = -1;
			if(currentMoveKey != -1 && !keys[currentMoveKey]) {
				if(keys[Keyboard.KEY_UP])
					currentMoveKey = Keyboard.KEY_UP;
				else if(keys[Keyboard.KEY_DOWN])
					currentMoveKey = Keyboard.KEY_DOWN;
				else if(keys[Keyboard.KEY_LEFT])
					currentMoveKey = Keyboard.KEY_LEFT;
				else if(keys[Keyboard.KEY_RIGHT])
					currentMoveKey = Keyboard.KEY_RIGHT;
			}
			// Switch to the next background/foreground tile
			if(keys[Keyboard.KEY_E]) {
				ply.getInventory().getQuickbar().nextSlot();
			}
			// Switch to the previous background/foreground tile
			if(keys[Keyboard.KEY_Q]) {
				ply.getInventory().getQuickbar().prevSlot();
			}
			// Set spawnpoint
			if(keys[Keyboard.KEY_RETURN])
				ply.getLevel().setSpawnpoint(ply.getPosition());
			// Return to spawnpoint
			if(keys[Keyboard.KEY_R])
				ply.setPosition(ply.getLevel().getSpawnpoint());
			if(keys[Keyboard.KEY_ESCAPE]) 
				parent.openScreen(new InventoryScreen());
			if(keys[Keyboard.KEY_T])
				parent.openChat();
			// Saving
			if(keys[Keyboard.KEY_LCONTROL] && keys[Keyboard.KEY_S]) {
				ply.getLevel().save();
				return; // Don't let Ctrl+S also make the player move!
			}
		}
		// Handle movement
		if(System.currentTimeMillis() >= lastMoveTime + 0 || keys[Keyboard.KEY_LSHIFT]) {
			if(ply.move(currentMoveKey, keys[Keyboard.KEY_LSHIFT])) {
				EventManager.manager.onPlayerMove(parent, ply.getPosition(), ply.getFacing());
				click(ply, Mouse.getX(), Mouse.getY());
				lastMoveTime = System.currentTimeMillis();
			}
		}
	}
	
	private void click(Player ply, int mouseX, int mouseY) {
		if(!mouse[0] && !mouse[1])
			return;
		Level level = ply.getLevel();
		Location pos = ply.getPosition();
		Location tpos = ply.getTilePosition();
		// Calculate at what offset to begin rendering the level
		Location offset = new Location(
				tpos.x - Display.getWidth() / Tile.TILESIZE / 2,
				tpos.y - Display.getHeight() / Tile.TILESIZE / 2);
		int xo = (pos.x % Entity.POSITIONS_PER_TILE);
		int yo = (pos.y % Entity.POSITIONS_PER_TILE);
		// tx and ty are the coordinates for the tile under the mouse cursor
		int tx = (mouseX + xo) / Tile.TILESIZE + offset.x;
		int ty = ((Display.getHeight() - mouseY) + yo) / Tile.TILESIZE + offset.y;
		
		if(tx < 0 || ty < 0 || tx >= level.getWidth() || ty >= level.getHeight())
			return;
		
		if(mouse[1]) {
			int fg = level.getFgId(tx, ty);
			if(fg != -1 && fg != TypeId.AIR) {
				level.getFg(tx, ty).hit(level, ply, tx, ty);
			}
			else {
				level.getBg(tx, ty).hit(level, ply, tx, ty);
			}
		}
		else if(mouse[0] && tx >= 0 && ty >= 0 && tx < level.getWidth() && ty < level.getHeight()) {
			if(ply.getHeldItem() instanceof TileItem) {
				TileItem it = (TileItem) ply.getHeldItem();
				if(it.getTile().isForeground()) {
					if(level.getFgId(tx, ty) == TypeId.AIR && tx != ply.getTilePosition().x && ty != ply.getTilePosition().y) { 
						level.setFg(tx, ty, it.getTile());
						level.setFgData(tx, ty, it.getData());
						ply.getInventory().removeOneItem(ply.getInventory().getQuickbar().getSelectedSlot());
					}
				}
				else {
					if(level.getBgId(tx, ty) != it.getTile().id) {
						level.setBg(tx, ty, it.getTile());
						level.setBgData(tx, ty, it.getData());
						ply.getInventory().removeOneItem(ply.getInventory().getQuickbar().getSelectedSlot());
					}
				}
			}
		}
	}

	@Override
	public void render(Screen screen) {
		Player player = parent.getPlayer();
		screen.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), Color.magenta);
		Level level = player.getLevel();
		Location pos = player.getPosition();
		Location tpos = player.getTilePosition();
		// Calculate at what offset to begin rendering the level
		Location renderStart = new Location(
				tpos.x - Display.getWidth() / Tile.TILESIZE / 2,
				tpos.y - Display.getHeight() / Tile.TILESIZE / 2);
		// Render the level
		screen.setOffset((pos.x % Entity.POSITIONS_PER_TILE), (pos.y % Entity.POSITIONS_PER_TILE));
		if(level != null) {
			level.render(screen, player, renderStart.x, renderStart.y, Display.getWidth() / Tile.TILESIZE + 1, Display.getHeight() / Tile.TILESIZE + 1);
		}
		if(active)
			renderMouse(screen, renderStart);
		screen.setOffset(0, 0);
		// Render the player sprite
		player.render(screen, level, (tpos.x - renderStart.x) * Entity.POSITIONS_PER_TILE, (tpos.y - renderStart.y) * Entity.POSITIONS_PER_TILE);
		// Render the HUD
		player.getInventory().getQuickbar().render(screen);
	}
	
	private void renderMouse(Screen screen, Location renderStart) {
		Player player = parent.getPlayer();
		Level level = player.getLevel();
		Item current = player.getHeldItem();
		Color col = transparentGreen;
		int tx = (Mouse.getX() + screen.getOffsetX()) / Tile.TILESIZE + renderStart.x;
		int ty = ((Display.getHeight() - Mouse.getY()) + screen.getOffsetY()) / Tile.TILESIZE + renderStart.y;
		if(current == null // Selected item is null
				|| (tx == player.getPosition().x && ty == player.getPosition().y) // Cursor is over the player
				|| tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight()) // Mouse it outside the bounds of the Level
			col = transparentRed;
		
		tx -= renderStart.x;
		ty -= renderStart.y;
		if(current != null) {
			col.bind();
			current.render(screen, tx * Tile.TILESIZE, ty * Tile.TILESIZE);
		}
		else {
			screen.renderFilledRect(tx * Tile.TILESIZE, ty * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, col);
		}
		// Draw border
		screen.renderRect(tx * Tile.TILESIZE, ty * Tile.TILESIZE, Tile.TILESIZE, Tile.TILESIZE, Color.black);
	}

}
