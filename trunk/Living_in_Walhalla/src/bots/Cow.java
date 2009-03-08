package bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import aStar2D.AStarMultiThread;
import aStar2D.Node;
import applets.BattleField;

public class Cow extends ICharacter {

	private Node[] waypoints;
	public static Random rand = new Random();

	public Cow(BattleField battleField, AStarMultiThread star, Node startupPosition, Node[] waypoints) {
		super(battleField, star, startupPosition);
		this.waypoints = waypoints;
		updatePosition();
		journeyDone();
	}

	@Override
	public float botRadius() {
		return 3;
	}

	@Override
	public float getSpeed() {
		return 0.5F;
	}

	@Override
	public void draw(Graphics g) {
		if (coord == null)
			return;
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(Color.white);
		g.fillRect((int) (x - botRadius()), y - 1, ((int) botRadius()) << 1, 2);
		g.setColor(Color.black);
	}

	@Override
	public String getName() {
		return "A cow";
	}

	@Override
	protected void journeyDone() {
		this.setDestination(waypoints[rand.nextInt(waypoints.length)]);
	}
}
