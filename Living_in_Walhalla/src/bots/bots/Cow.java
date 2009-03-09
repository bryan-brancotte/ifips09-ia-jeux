package bots.bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import bots.ICharacter;
import bots.ITeam;

import aStar2D.AStarMultiThread;
import aStar2D.Node;
import applets.BattleField;

public class Cow extends ICharacter {

	private Node[] waypoints;
	public static Random rand = new Random();

	/**
	 * L'équipe des vaches : la nature. P.S: elle aime personne...
	 */
	public static ITeam nature = new ITeam() {
		@Override
		public String getName() {
			return "nature";
		}

		@Override
		public boolean isOpposedTo(ITeam team) {
			return this != team;
		}
	};

	public Cow(BattleField battleField, AStarMultiThread star, Node startupPosition, Node[] waypoints) {
		super(battleField, star, startupPosition, nature);
		this.waypoints = waypoints;
		updatePosition();
		journeyDone();
	}

	@Override
	public float botRadius() {
		return 5;
	}

	@Override
	public float getSpeed() {
		return 1F;
	}

	@Override
	public void drawCharacter(Graphics g) {
		if (coord == null)
			return;
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(Color.white);
		g.fillPolygon(new int[] { x - (int) botRadius(), x - 2, x + 2, x + (int) botRadius() }, new int[] {
				y - (int) botRadius(), y + (int) botRadius(), y + (int) botRadius(), y - (int) botRadius() }, 4);
		g.setColor(Color.yellow);
		g.fillRect(x - 2 + (int) botRadius(), (int) (y - botRadius() - 2), 2, 2);
		g.fillRect((int) (x - botRadius()), (int) (y - botRadius() - 2), 2, 2);
		g.setColor(Color.black);
		g.drawLine(x - (int) botRadius() + 2, y + 2 - (int) botRadius(), x - (int) botRadius() + 3, y + 2
				- (int) botRadius());
		g.drawLine(x + (int) botRadius() - 2, y + 2 - (int) botRadius(), x + (int) botRadius() - 3, y + 2
				- (int) botRadius());
	}

	@Override
	public String getName() {
		return "A cow";
	}

	@Override
	protected void journeyDone() {
		this.setDestination(waypoints[rand.nextInt(waypoints.length)]);
	}

	@Override
	public boolean isDead() {
		return false;
	}
}
