package com.github.dingledooper.spaceinvaders;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;

public class Sprite {

	private int x, y, width, height;
	private boolean isRemoved;
	private Image image;

	public Sprite(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}
	
	public void draw(Graphics g) {
		g.drawImage(getImage(), getX(), getY(), getWidth(), getHeight(), null);
	}
	
	public boolean isIntersecting(int x0, int y0, int w0, int h0, int x1, int y1, int w1, int h1) {
		Rectangle rect1 = new Rectangle(x0, y0, w0, h0), rect2 = new Rectangle(x1, y1, w1, h1);
		return rect1.intersects(rect2);
	}
	
	public int getX() {
		return x;
	}
	
	public int getY() {
		return y;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Image getImage() {
		return image;
	}
	
	public boolean isRemoved() {
		return isRemoved;
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public void setWidth(int width) {
		this.width = width;
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
	
	public void setImage(Image image) {
		this.image = image;
	}
	
	public void setRemoved(boolean isRemoved) {
		this.isRemoved = isRemoved;
	}
	
}