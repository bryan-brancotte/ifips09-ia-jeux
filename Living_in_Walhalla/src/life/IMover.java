package life;

import java.awt.Graphics;

import utils.Vector2d;

public interface IMover {

	public abstract void draw(Graphics g);

	public abstract boolean moveInItsDirection();

	public abstract ITeam getTeam();
	
	public boolean isDead();

}