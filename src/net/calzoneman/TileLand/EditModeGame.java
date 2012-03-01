package net.calzoneman.TileLand;



import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.player.Player;
import net.calzoneman.TileLand.screen.EditMainScreen;
import net.calzoneman.TileLand.screen.GameScreen;

public class EditModeGame extends Game {
	protected Player player;
	protected EditMainScreen editScreen;
	protected GameScreen currentScreen = null;
	
	public EditModeGame(Player player) {
		super(player);
		this.editScreen = new EditMainScreen(this);
		mainScreen.setActive(false);
	}
	
	public boolean isMultiplayer() {
		return false;
	}
	
	public void openScreen(GameScreen scr) {
		scr.setParent(this);
		// Turn over input to the screen
		scr.setActive(true);
		this.currentScreen = scr;
		editScreen.setActive(false);
	}
	
	public void closeScreen() {
		if(currentScreen != null) {
			currentScreen.resetInput();
			currentScreen.onClosing();
		}
		currentScreen = null;
		editScreen.setActive(true);
		editScreen.resetInput();
	}

	public void tick() {
		if(editScreen.isActive()) {
			editScreen.handleInput();
		}
		else if(currentScreen != null && currentScreen.isActive()) {
			currentScreen.handleInput();
		}
		else if(currentScreen != null) {
			closeScreen();
		}
	}

	public void render(Screen screen) {
		editScreen.render(screen);
		if(currentScreen != null && currentScreen.isActive())
			currentScreen.render(screen);
	}
}
