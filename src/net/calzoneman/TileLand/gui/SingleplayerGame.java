package net.calzoneman.TileLand.gui;

import org.lwjgl.opengl.Display;
import org.newdawn.slick.Color;

import net.calzoneman.TileLand.EditModeGame;
import net.calzoneman.TileLand.Game;
import net.calzoneman.TileLand.ResourceManager;
import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gui.NewLevelMenu.GameParameters;
import net.calzoneman.TileLand.level.BasicLevelGenerator;
import net.calzoneman.TileLand.level.Level;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.player.EditModePlayer;

public class SingleplayerGame extends GUIMenu {
		
	private Game game = null;
	private boolean levelError = false;
	
	public SingleplayerGame(GameParameters gp) {
		if(gp == null)
			gp = GameParameters.defaultGameParameters;
		Level lvl = new BasicLevelGenerator().generate(gp.width, gp.height);
		lvl.setName(gp.levelName);
		Player ply = gp.editMode ? new EditModePlayer(ResourceManager.PLAYER_TEXTURE, lvl, gp.playerName) : new Player(ResourceManager.PLAYER_TEXTURE, lvl, gp.playerName);
		game = gp.editMode ? new EditModeGame(ply) : new Game(ply);
	}
	
	public SingleplayerGame(String lvlName, String playerName, boolean editMode) {
		Level lvl = new Level(lvlName);
		levelError = !lvl.initialized;
		if(levelError)
			return;
		Player ply;
		if(editMode)
			ply = new EditModePlayer(ResourceManager.PLAYER_TEXTURE, lvl, playerName);
		else
			ply = new Player(ResourceManager.PLAYER_TEXTURE, lvl, playerName);
		if(editMode)
			game = new EditModeGame(ply);
		else
			game = new Game(ply);
	}
	
	@Override
	public void render(Screen screen) {
		if(game != null)
			game.render(screen);
		else if(levelError) {
			String[] levelErrorMessage = { "Uh Oh!  The level is broken!", "Make sure it's not an outdated file!" };
			screen.renderFilledRect(0, 0, Display.getWidth(), Display.getHeight(), Color.red);
			Font.drawLarge(levelErrorMessage[0], screen, (Display.getWidth() - Font.getWidthLarge(levelErrorMessage[0])) / 2, Display.getHeight()/2);
			Font.drawLarge(levelErrorMessage[1], screen, (Display.getWidth() - Font.getWidthLarge(levelErrorMessage[1])) / 2, Display.getHeight()/2);
		}
	}
	
	@Override
	public void handleInput() {
		if(game != null)
			game.tick();
	}
}
