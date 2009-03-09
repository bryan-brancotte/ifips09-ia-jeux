package bots.bots;

import java.awt.Color;
import java.awt.Graphics;

import bots.ICharacter;
import bots.ITeam;

import aStar2D.AStarMultiThread;
import aStar2D.Node;
import applets.BattleField;

public class Personnage extends ICharacter {

	protected String name;

	protected Color myColor;

	public Personnage(BattleField battleField, AStarMultiThread star, Color color, String name, ITeam team) {
		super(battleField, star, new Node(10,100),team);
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
		return 3F;
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
