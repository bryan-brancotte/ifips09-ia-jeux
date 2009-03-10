package life.bots;

import java.awt.Color;
import java.awt.Graphics;

import life.ICharacter;
import life.ITeam;
import aStar2D.Node;

public class Personnage extends ICharacter {

	protected String name;

	protected Color myColor;

	public Personnage(Color color, String name, ITeam team) {
		super(new Node(10, 100), team);
		this.name = name;
		this.myColor = color;
	}

	@Override
	public float botRadius() {
		return 3;
	}

	@Override
	public void drawCharacter(Graphics g) {
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(myColor);
		g.drawLine((int) (x - botRadius()), y, (int) (x + botRadius()), y);
		g.drawLine(x, (int) (y - botRadius()), x, (int) (y + botRadius()));
		g.drawOval((int) (coord.x - botRadius()), (int) (coord.y - botRadius()), (int) (botRadius() * 2),
				(int) (botRadius() * 2));
		g.setColor(Color.black);

	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public float getSpeed() {
		return 1.5F;
	}

	@Override
	protected void journeyDone() {
	}

	@Override
	public boolean isDead() {
		// TODO Auto-generated method stub
		return false;
	}

}
