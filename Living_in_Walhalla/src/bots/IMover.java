package bots;

import java.awt.Graphics;

import utils.Vector2d;

public interface IMover {

	public abstract void draw(Graphics g);

	public abstract void moveTo(Vector2d d);

	public abstract boolean moveInItsDirection();

}