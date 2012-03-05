package net.calzoneman.TileLand.screen;


import java.io.IOException;

import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.entity.Entity;
import net.calzoneman.TileLand.entity.Mob.MoveDirection;
import net.calzoneman.TileLand.event.EventManager;
import net.calzoneman.TileLand.gfx.AnimatedSprite;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.gfx.MobSprite;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.level.Location;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.tile.Tile;
import net.calzoneman.TileLand.tile.TileTypes;
import net.calzoneman.TileLand.tile.TypeId;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;
import org.newdawn.slick.opengl.Texture;

public class MainScreen extends GameScreen {
	
	static final int KEY_UP = Keyboard.KEY_W;
	static final int KEY_DOWN = Keyboard.KEY_S;
	static final int KEY_LEFT = Keyboard.KEY_A;
	static final int KEY_RIGHT = Keyboard.KEY_D;
	static final int KEY_HIT = Keyboard.KEY_RETURN;
	
	private AnimatedSprite coinSprite;
	private Texture hitSprite;
	private int currentMoveKey;
		
	public MainScreen(Game parent) {
		super(0, 0, Display.getWidth(), Display.getHeight(), parent);
		this.currentMoveKey = -1;
		try {
			coinSprite = new AnimatedSprite(
					ResourceManager.ITEMSHEET.getCustomTileTexture(0, 0, 4, 1), 32, 100);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
		try {
			hitSprite = ResourceManager.MISCSHEET.getTileTexture(0);
		} 
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void handleInput() {
		handleMouse();
		handleKeyboard();
		parent.getPlayer().getLevel().tick();
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
			if(keys[Keyboard.KEY_ESCAPE]) 
				parent.openScreen(new InventoryScreen());
			//if(keys[Keyboard.KEY_T])
			//	parent.openChat();
			if(keys[KEY_HIT]) {
				Level level = ply.getLevel();
				Location position = ply.getTilePosition();
				if(ply.getFacing() == MobSprite.FACING_UP)
					position.y--;
				else if(ply.getFacing() == MobSprite.FACING_DOWN)
					position.y++;
				else if(ply.getFacing() == MobSprite.FACING_LEFT)
					position.x--;
				else if(ply.getFacing() == MobSprite.FACING_RIGHT)
					position.x++;
				int id = level.getFgId(position.x, position.y);
				if(id != TypeId.AIR)
					TileTypes.getTile(id).hit(level, ply, position.x, position.y);
				// TODO Hit entities
			}
		}
		// Handle movement
		if(ply.move(getDirection(currentMoveKey), keys[Keyboard.KEY_LSHIFT])) {
			EventManager.manager.onPlayerMove(parent, ply.getPosition(), ply.getFacing());
			click(ply, Mouse.getX(), Mouse.getY());
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
			level.renderFog(screen, player, renderStart.x, renderStart.y, Display.getWidth() / Tile.TILESIZE + 1, Display.getHeight() / Tile.TILESIZE + 1);
		}
		screen.setOffset(0, 0);
		// Render the player sprite
		player.render(screen, level, (tpos.x - renderStart.x) * Entity.POSITIONS_PER_TILE, (tpos.y - renderStart.y) * Entity.POSITIONS_PER_TILE);
		
		renderHud(screen);
	}
	
	private void renderHud(Screen screen) {
		Player ply = parent.getPlayer();
		
		if(keys[KEY_HIT]) {
			int x = Display.getWidth() / 2 - ply.getSprite().getWidth() / 2;
			int y = Display.getHeight() / 2 - ply.getSprite().getHeight() / 2;
			int facing = ply.getFacing();
			if(facing == MobSprite.FACING_UP)
				y -= 42;
			else if(facing == MobSprite.FACING_RIGHT)
				x += 32;
			else if(facing == MobSprite.FACING_DOWN)
				y += 32;
			else if(facing == MobSprite.FACING_LEFT)
				x -= 32;
			screen.renderTexture(hitSprite, x, y);
		}
		
		int cx = 32 - coinSprite.getWidth()/2;
		int cy = 32 - coinSprite.getHeight()/2;
		int sx = cx + coinSprite.getWidth();
		int sy = 32 + (Font.getHeightLarge(""+ply.getCoins()) - coinSprite.getHeight())/2;
		coinSprite.render(screen, cx, cy);
		Font.drawLarge(""+ply.getCoins(), screen, sx, sy);
	}
}
