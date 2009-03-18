package life;

import java.awt.Color;
import java.awt.Graphics;

import aStar2D.Node;

import utils.Vector2d;

public interface IMover {

	public abstract void draw(Graphics g);

	public abstract boolean moveInItsDirection();

	public abstract ITeam getTeam();

	public boolean isDead();

	public float getRadius();

	public Vector2d getCoord();

	public Node getNode();

	public void hit(int damage);

	public Color getColor();

}