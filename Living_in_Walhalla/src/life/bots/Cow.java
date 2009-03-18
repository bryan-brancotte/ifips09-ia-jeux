package life.bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import life.ICharacter;
import life.ITeam;
import aStar2D.Node;

public class Cow extends ICharacter {
	public static int MY_LIFE = 1000;

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

		@Override
		public Color getColor() {
			return Color.green;
		}

		@Override
		public void registerPlayer(ICharacter character) {
		}

		@Override
		public int draw(Graphics g, int x, int y, int heigth) {
			return 0;
		}
	};

	public Cow(Node startupPosition, Node[] waypoints) {
		super(startupPosition, nature, "Cow");
		this.waypoints = waypoints;
		updatePosition();
		journeyDone();
		life = getInitialLife();
	}

	@Override
	public float getRadius() {
		return 5;
	}

	@Override
	public float getSpeed() {
		return 0.2F;
	}

	@Override
	public void drawCharacter(Graphics g) {
		if (coord == null)
			return;
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(Color.white);
		g.fillPolygon(new int[] { x - (int) getRadius(), x - 2, x + 2, x + (int) getRadius() }, new int[] {
				y - (int) getRadius(), y + (int) getRadius(), y + (int) getRadius(), y - (int) getRadius() }, 4);
		g.setColor(Color.yellow);
		g.fillRect(x - 2 + (int) getRadius(), (int) (y - getRadius() - 2), 2, 2);
		g.fillRect((int) (x - getRadius()), (int) (y - getRadius() - 2), 2, 2);
		g.setColor(Color.black);
		g.drawLine(x - (int) getRadius() + 2, y + 2 - (int) getRadius(), x - (int) getRadius() + 3, y + 2
				- (int) getRadius());
		g.drawLine(x + (int) getRadius() - 2, y + 2 - (int) getRadius(), x + (int) getRadius() - 3, y + 2
				- (int) getRadius());
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

	@Override
	public Color getColor() {
		return nature.getColor();
	}
}
