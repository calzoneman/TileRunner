package net.calzoneman.TileLand.inventory;

import org.newdawn.slick.Color;

import net.calzoneman.TileLand.gfx.Screen;
import net.calzoneman.TileLand.gfx.Font;

public class ItemStack implements Cloneable {
	public static final int MAX_STACK_SIZE = 100;
	
	private Item item;
	private byte data;
	private int count;
	
	public ItemStack(Item it) {
		this.item = it;
		this.count = 1;
		this.data = 0;
	}
	
	public ItemStack(Item it, int count) {
		this.item = it;
		if(count < 0)
			count = 0;
		else if(count <= MAX_STACK_SIZE)
			this.count = count;
		else
			this.count = MAX_STACK_SIZE;
		this.data = 0;
	}
	
	public ItemStack(Item it, int count, int data) {
		this.item = it;
		if(count < 0)
			count = 0;
		else if(count <= MAX_STACK_SIZE)
			this.count = count;
		else
			this.count = MAX_STACK_SIZE;
		this.data = (byte) data;
	}
	
	public Item getItem() {
		return item;
	}
	
	public byte getData() {
		return data;
	}
	
	public int getCount() {
		return count;
	}
	
	public void setData(int data) { 
		this.data = (byte) data;
	}
	
	public void setCount(int count) {
		if(count >= 0 && count <= MAX_STACK_SIZE)
			this.count = count;
	}
	
	@Override
	public ItemStack clone() {
		return new ItemStack(item, count, data);
	}

	public void render(Screen screen, int x, int y) {
		// Draw the item
		if(item != null) {
			item.render(screen, x, y);
		}
		
		// Draw the count
		if(count > 1)
			Font.draw(Font.TEXT_YELLOW + count, screen, x, y, Color.black);
	}
}
