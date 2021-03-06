package net.calzoneman.TileLand.gui;

import org.lwjgl.opengl.Display;

import net.calzoneman.TileLand.gfx.Font;
import net.calzoneman.TileLand.util.Delegate;

public class NewLevelMenu extends GUIMenu {
	
	private GameParameters gameParameters;
	
	public NewLevelMenu() {
		super();
		init(0, 0, Display.getWidth(), Display.getHeight());
	}
	
	@Override
	public void init(int x, int y, int width, int height) {
		GUIContainer container = new GUIContainer(Display.getWidth()/2 - 320, Display.getHeight()/2 - 240, 640, 480);
		int x1 = 175;
		int x2 = x1 + 100;
		int curY = 145;
		int charHeight = Font.getHeight("|");
		
		GUITextbox plyNameTxt = new GUITextbox(x2, curY, 190, "Player");
		plyNameTxt.setMaxLength(32);
		container.addChild("plynametxt", plyNameTxt);
		GUILabel plyNameLbl = new GUILabel(x1, curY + plyNameTxt.getHeight()/2 - charHeight/2, "Player name");
		container.addChild("plynamelbl", plyNameLbl);
		curY += plyNameTxt.getHeight() + 10;
		
		GUITextbox lvlWidthTxt = new GUITextbox(x2, curY, 190, "100");
		lvlWidthTxt.setMaxLength(10);
		container.addChild("lvlwidthtxt", lvlWidthTxt);
		GUILabel lvlWidthLbl = new GUILabel(x1, curY + lvlWidthTxt.getHeight()/2 - charHeight/2, "Level width");
		container.addChild("lvlwidthlbl", lvlWidthLbl);
		curY += lvlWidthTxt.getHeight() + 10;
		
		GUITextbox lvlHeightTxt = new GUITextbox(x2, curY, 190, "100");
		lvlHeightTxt.setMaxLength(10);
		container.addChild("lvlheighttxt", lvlHeightTxt);
		GUILabel lvlHeightLbl = new GUILabel(x1, curY + lvlHeightTxt.getHeight()/2 - charHeight/2, "Level height");
		container.addChild("lvlheightlbl", lvlHeightLbl);
		curY += lvlHeightTxt.getHeight() + 10;
		
		GUITextbox lvlNameTxt = new GUITextbox(x2, curY, 190, "untitled");
		lvlNameTxt.setMaxLength(32);
		container.addChild("lvlnametxt", lvlNameTxt);
		GUILabel lvlNameLbl = new GUILabel(x1, curY + lvlNameTxt.getHeight()/2 - charHeight/2, "Level name");
		container.addChild("lvlnamelbl", lvlNameLbl);
		curY += lvlNameTxt.getHeight() + 10;
		
		GUICheckbox editCheck = new GUICheckbox(x2, curY);
		container.addChild("editcheck", editCheck);
		GUILabel editLbl = new GUILabel(x1, curY + editCheck.getHeight() / 2 - charHeight / 2, "Edit Mode");
		container.addChild("editlbl", editLbl);
		curY += editCheck.getHeight() + 10;
		
		GUIButton cancelBtn = new GUIButton(x1, curY, 135, "Cancel");
		cancelBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						MenuManager.getMenuManager().goBack();
						return null;
					}
				});
		container.addChild("cancelbtn", cancelBtn);
		
		GUIButton createBtn = new GUIButton(x1+155, curY, 135, "Create Level");
		createBtn.setClickHandler(
				new Delegate<GUIContainer, Void>() {
					@Override
					public Void run(GUIContainer param) {
						((NewLevelMenu) param.getParent()).createLevel();
						return null;
					}
				});
		container.addChild("createbtn", createBtn);
		curY += createBtn.getHeight() + 10;
		
		GUILabel widtherror = new GUILabel(x1, curY, "");
		container.addChild("widtherror", widtherror);
		curY += widtherror.getHeight() + 10;
		
		GUILabel heighterror = new GUILabel(x1, curY, "");
		container.addChild("heighterror", heighterror);
		curY += heighterror.getHeight() + 10;
		
		GUILabel levelnameerror = new GUILabel(x1, curY, "");
		container.addChild("levelnameerror", levelnameerror);
		curY += levelnameerror.getHeight() + 10;
		
		container.setFieldOrder(new String[] { "plynametxt", "lvlwidthtxt", "lvlheighttxt", "lvlnametxt" });
		container.setParent(this);
		addChild("container", container);
	}
	
	@Override
	public void reInit(int x, int y, int width, int height) {
		GUIContainer container = (GUIContainer) getChild("container");
		String plyName = ((GUITextbox) container.getChild("plynametxt")).getTextOrDefault();
		String lvlWidth = ((GUITextbox) container.getChild("lvlwidthtxt")).getTextOrDefault();
		String lvlHeight = ((GUITextbox) container.getChild("lvlheighttxt")).getTextOrDefault();
		String lvlName = ((GUITextbox) container.getChild("lvlnametxt")).getTextOrDefault();
		children.clear();
		
		init(x, y, width, height);
		
		container = (GUIContainer) getChild("container");
		((GUITextbox) container.getChild("plynametxt")).setText(plyName);
		((GUITextbox) container.getChild("lvlwidthtxt")).setText(lvlWidth);
		((GUITextbox) container.getChild("lvlheighttxt")).setText(lvlHeight);
		((GUITextbox) container.getChild("lvlnametxt")).setText(lvlName);
	}
	
	@Override
	protected void onEnter() {
		createLevel();
	}
	
	public void createLevel() {
		if(validate())
			MenuManager.getMenuManager().openMenu("singleplayergame");
	}
	
	public void clearErrors() {
		GUIContainer container  = (GUIContainer) getChild("container");
		((GUILabel) container.getChild("widtherror")).setText("");
		((GUILabel) container.getChild("heighterror")).setText("");
		((GUILabel) container.getChild("levelnameerror")).setText("");
	}
	
	public boolean validate() {
		clearErrors();
		GUIContainer container  = (GUIContainer) getChild("container");
		int w = -1;
		int h = -1;
		String lvlName = "";
		String plyName = "";
		boolean error = false;
		try {
			w = Integer.parseInt(((GUITextbox) container.getChild("lvlwidthtxt")).getText());
		}
		catch(Exception ex) {
			((GUILabel) container.getChild("widtherror")).setText(Font.TEXT_DARK_RED + "Invalid level width");
			error = true;
		}
		try {
			h = Integer.parseInt(((GUITextbox) container.getChild("lvlheighttxt")).getText());
		}
		catch(Exception ex) {
			((GUILabel) container.getChild("heighterror")).setText(Font.TEXT_DARK_RED + "Invalid level height");
			error = true;
		}
		lvlName = ((GUITextbox) container.getChild("lvlnametxt")).getText();
		if(lvlName.isEmpty()) {
			((GUILabel) container.getChild("levelnameerror")).setText(Font.TEXT_DARK_RED + "Invalid level name");
			error = true;
		}
		plyName = ((GUITextbox) container.getChild("plynametxt")).getText();
		if(w < 0) {
			((GUILabel) container.getChild("widtherror")).setText(Font.TEXT_DARK_RED + "Invalid level width");
			error = true;
		}
		if(h < 0) {
			((GUILabel) container.getChild("heighterror")).setText(Font.TEXT_DARK_RED + "Invalid level height");
			error = true;
		}
		
		boolean edit = ((GUICheckbox) container.getChild("editcheck")).isChecked();
		gameParameters = new GameParameters(w, h, lvlName, plyName, edit);
		return !error;
	}
	
	public GameParameters getGameParameters() {
		return gameParameters;
	}
	
	public static class GameParameters {
		public int width;
		public int height;
		public String levelName;
		public String playerName;
		public boolean editMode;
		
		public static final GameParameters defaultGameParameters = new GameParameters(100, 100, "untitled", "Player", false);
		
		public GameParameters(int width, int height, String levelName, String playerName, boolean edit) {
			this.width = width;
			this.height = height;
			this.levelName = levelName;
			this.playerName = playerName;
			this.editMode = edit;
		}
	}
}
