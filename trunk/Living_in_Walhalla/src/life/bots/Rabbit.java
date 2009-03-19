package life.bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import life.ICharacter;
import aStar2D.Node;

public class Rabbit extends ICharacter {
	public static int MY_LIFE = 11;

	private Node[] waypoints;
	public static Random rand = new Random();

	public Rabbit(Node startupPosition, Node[] waypoints) {
		super(startupPosition, Cow.nature, "Rabit");
		this.waypoints = waypoints;
		updatePosition();
		journeyDone();
		life = getInitialLife();
	}

	@Override
	public float getRadius() {
		return 4;
	}

	@Override
	public float getSpeed() {
		return 2F;
	}

	@Override
	public void drawCharacter(Graphics g) {
		if (coord == null)
			return;
		int x = (int) coord.x;
		int y = (int) coord.y;
		int radius = (int) getRadius();
		g.setColor(Color.white);
		g.fillRect(x - radius, y - 2, radius << 1, (radius - 2) << 1);
		g.drawLine(x, y - radius, x + radius, y);
		g.setColor(Color.black);
		g.drawLine(x + radius - 1, y - 1, x + radius - 1, y - 1);
	}

	@Override
	protected void journeyDone() {
		this.setDestination(waypoints[rand.nextInt(waypoints.length)]);
	}

	@Override
	public boolean isDead() {
		return iAmDead;
	}

	@Override
	protected void canShoot() {
	}

	@Override
	public int getInitialLife() {
		return MY_LIFE;
	}
}
