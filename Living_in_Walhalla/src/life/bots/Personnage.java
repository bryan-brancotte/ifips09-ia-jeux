package life.bots;

import java.awt.Color;
import java.awt.Graphics;

import life.ICharacter;
import life.ITeam;
import aStar2D.Node;

public class Personnage extends ICharacter {

	public Personnage(String name, ITeam team) {
		super(new Node(10, 100), team, name);
	}

	@Override
	public float getRadius() {
		return 3;
	}

	@Override
	public void drawCharacter(Graphics g) {
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(team.getColor());
		g.drawLine((int) (x - getRadius()), y, (int) (x + getRadius()), y);
		g.drawLine(x, (int) (y - getRadius()), x, (int) (y + getRadius()));
		g.drawOval((int) (coord.x - getRadius()), (int) (coord.y - getRadius()), (int) (getRadius() * 2),
				(int) (getRadius() * 2));
		g.setColor(Color.black);

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getSpeed() {
		return 1F;
	}

	@Override
	protected void journeyDone() {
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public void hit(int damage) {
	}

	@Override
	protected void canShoot() {
	}

	@Override
	public int getInitialLife() {
		return 0;
	}

}
