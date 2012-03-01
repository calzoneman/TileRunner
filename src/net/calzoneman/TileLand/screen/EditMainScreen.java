package net.calzoneman.TileLand.screen;


import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.entity.Mob.MoveDirection;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.inventory.Item;
import net.calzoneman.TileLand.inventory.ItemStack;
import net.calzoneman.TileLand.inventory.TileItem;
import net.calzoneman.TileLand.inventory.Tilelist;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TypeId;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

public class EditMainScreen extends GameScreen {
	
	/** Colors for Mouse overlay */
	private static Color transparentRed = new Color(255, 0, 0, 130);
	private static Color transparentGreen = new Color(0, 255, 0, 130);
	
	static final int KEY_UP = Keyboard.KEY_W;
	static final int KEY_DOWN = Keyboard.KEY_S;
	static final int KEY_LEFT = Keyboard.KEY_A;
	static final int KEY_RIGHT = Keyboard.KEY_D;
	static final int KEY_NEXT = Keyboard.KEY_E;
	static final int KEY_PREV = Keyboard.KEY_Q;
	static final int KEY_SAVE = Keyboard.KEY_F1;
	static final int KEY_SETSPAWN = Keyboard.KEY_RETURN;
	static final int KEY_RESPAWN = Keyboard.KEY_R;
	
	static final long MESSAGE_FADE_DELAY = 5000L;
	
	private int currentMoveKey;
	private Tilelist tilelist;
	private long messageTime;
	private String message = "";
	
	public EditMainScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
		this.currentMoveKey = -1;
		tilelist = new Tilelist();
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
						case KEY_UP:
						case KEY_DOWN:
						case KEY_LEFT:
						case KEY_RIGHT:
							currentMoveKey = Keyboard.getEventKey();
							break;
						default:
							break;
					}
				}
			}
			if(!keys[KEY_UP] && !keys[KEY_DOWN] && !keys[KEY_LEFT] && !keys[KEY_RIGHT])
				currentMoveKey = -1;
			if(currentMoveKey != -1 && !keys[currentMoveKey]) {
				if(keys[KEY_UP])
					currentMoveKey = KEY_UP;
				else if(keys[KEY_DOWN])
					currentMoveKey = KEY_DOWN;
				else if(keys[KEY_LEFT])
					currentMoveKey = KEY_LEFT;
				else if(keys[KEY_RIGHT])
					currentMoveKey = KEY_RIGHT;
			}
			
			if(keys[KEY_NEXT])
				tilelist.nextSlot();
			else if(keys[KEY_PREV])
				tilelist.prevSlot();
			
			if(keys[KEY_SAVE]) {
				ply.getLevel().save();
				message = "Level saved";
				messageTime = System.currentTimeMillis();
			}
			
			if(keys[KEY_SETSPAWN]) {
				ply.getLevel().setSpawnpoint(ply.getTilePosition());
			}
			if(keys[KEY_RESPAWN]) {
				Location loc = new Location(ply.getLevel().getSpawnpoint());
				loc.x = loc.x * Entity.POSITIONS_PER_TILE + Entity.POSITIONS_PER_TILE / 2;
				loc.y = loc.y * Entity.POSITIONS_PER_TILE + Entity.POSITIONS_PER_TILE / 2;
				ply.setPosition(loc);
			}
			
		}
		// Handle movement
		if(ply.move(getDirection(currentMoveKey), keys[Keyboard.KEY_LSHIFT])) {
			EventManager.manager.onPlayerMove(parent, ply.getPosition(), ply.getFacing());
			click(ply, Mouse.getX(), Mouse.getY());
			System.currentTimeMillis();
		}
	}
	
	private int getDirection(int key) {
		switch(key) {
			case KEY_UP:
				return MoveDirection.UP;
			case KEY_DOWN:
				return MoveDirection.DOWN;
			case KEY_LEFT:
				return MoveDirection.LEFT;
			case KEY_RIGHT:
				return MoveDirection.RIGHT;
			default:
				return -1;
		}
	}
	
	private Location getTileOffset(Location tpos) {
		Location loc = new Location(
				tpos.x - Display.getWidth() / Tile.TILESIZE / 2,
				tpos.y - Display.getHeight() / Tile.TILESIZE / 2);
		return loc;
	}
	
	private void click(Player ply, int mouseX, int mouseY) {
		if(!mouse[0] && !mouse[1])
			return;
		Level level = ply.getLevel();
		Location pos = ply.getPosition();
		Location tpos = ply.getTilePosition();
		// Calculate at what offset to begin rendering the level
		Location offset = getTileOffset(tpos);
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
				level.setFgId(tx, ty, TypeId.AIR);
			}
			else {
				level.setBgId(tx, ty, TypeId.DIRT);
			}
		}
		else if(mouse[0] && tx >= 0 && ty >= 0 && tx < level.getWidth() && ty < level.getHeight()) {
			ItemStack its = tilelist.getSelectedItemStack();
			if(its == null)
				return;
			if(its.getItem() instanceof TileItem) {
				TileItem it = (TileItem) its.getItem();
				if(it.getTile().isForeground()) {
					if(level.getFgId(tx, ty) == TypeId.AIR && tx != ply.getTilePosition().x && ty != ply.getTilePosition().y) { 
						level.setFg(tx, ty, it.getTile());
						level.setFgData(tx, ty, it.getData());
					}
				}
				else {
					if(level.getBgId(tx, ty) != it.getTile().id) {
						level.setBg(tx, ty, it.getTile());
						level.setBgData(tx, ty, it.getData());
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
		Location renderStart = getTileOffset(tpos);
		screen.setOffset((pos.x % Entity.POSITIONS_PER_TILE), (pos.y % Entity.POSITIONS_PER_TILE));
		if(level != null) {
			level.render(screen, player, renderStart.x, renderStart.y, Display.getWidth() / Tile.TILESIZE + 1, Display.getHeight() / Tile.TILESIZE + 1);
		}
		if(active)
			renderMouse(screen, renderStart);
		screen.setOffset(0, 0);
		// Render the player sprite
		player.render(screen, level, (tpos.x - renderStart.x) * Entity.POSITIONS_PER_TILE, (tpos.y - renderStart.y) * Entity.POSITIONS_PER_TILE);
		
		renderHud(screen);
	}
	
	private void renderHud(Screen screen) {
		tilelist.render(screen);
		Font.drawLarge("EDIT MODE", screen, 0, Display.getHeight() - Font.getHeightLarge("EDIT MODE"), Color.black);
		
		if(!message.isEmpty() && System.currentTimeMillis() < messageTime + MESSAGE_FADE_DELAY) {
			Font.drawLarge(message, screen, Display.getWidth() - Font.getWidthLarge(message), 0);
		}
		else if(System.currentTimeMillis() >= messageTime + MESSAGE_FADE_DELAY) {
			message = "";
		}
	}
	
	private void renderMouse(Screen screen, Location renderStart) {
		Player player = parent.getPlayer();
		Level level = player.getLevel();
		ItemStack it = tilelist.getSelectedItemStack();
		Item current = (it == null) ? null : it.getItem();
		Color col = transparentGreen;
		int tx = (Mouse.getX() + screen.getOffsetX()) / Tile.TILESIZE + renderStart.x;
		int ty = ((Display.getHeight() - Mouse.getY()) + screen.getOffsetY()) / Tile.TILESIZE + renderStart.y;
		if(current == null || tx < 0 || tx >= level.getWidth() || ty < 0 || ty >= level.getHeight()) // Mouse it outside the bounds of the Level
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
