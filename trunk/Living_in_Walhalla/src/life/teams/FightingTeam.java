package life.teams;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import utils.Vector2d;

import life.ICharacter;
import life.IMover;
import life.IStrategie;
import life.ITeam;
import aStar2D.Node;

public class FightingTeam implements ITeam {

	private String name;
	private Color color;
	protected ArrayList<ICharacter> players;
	protected int idPLayer;
	protected IStrategie strategie;

	public FightingTeam(String name, Color color, IStrategie strategie) {
		super();
		this.name = name;
		this.color = color;
		this.players = new ArrayList<ICharacter>(8);
		strategie.worksFor(this);
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isOpposedTo(ITeam team) {
		return team != this;
	}

	@Override
	public Color getColor() {
		return color;
	}

	@Override
	public void registerPlayer(ICharacter character) {
		players.add(character);
	}

	@Override
	public String toString() {
		String ret = this.getName() + ":\t";
		for (ICharacter c : players)
			ret += c.getLife() + ",\t";
		return ret;
	}

	@Override
	public int draw(Graphics g, int x, int y, int heigth) {
		g.setColor(Color.black);
		g.drawRect(x, y, players.size() * 6 + 2, heigth);
		g.setColor(color);
		x -= 4;
		++y;
		for (ICharacter c : players)
			g.fillRect(x += 6, y + 1, 5, (int) ((float) c.getLife() / c.getInitialLife() * heigth - 3));
		return players.size() * 6 + 2;
	}

	@Override
	public void newOrders() {
		idPLayer = 0;
	}

	@Override
	public void attack(Node target, int qte) {
		for (; idPLayer < players.size() && qte-- != 0; idPLayer++) {
			players.get(idPLayer).setDestination(target);
		}

	}

	@Override
	public int getCountFighter() {
		return players.size();
	}

	@Override
	public boolean isNotFriendlyFire(IMover shooter, Vector2d dest) {
		float dstCarre = shooter.getCoord().distanceCarre(dest);
		dstCarre *= dstCarre;
		for (ICharacter c : players)
			if (shooter != c
					&& (c.getRadius() * c.getRadius() * 2 < c.getCoord().distanceCarre(dest)
							+ c.getCoord().distanceCarre(shooter.getCoord()) - dstCarre)) {
//				System.out.println("Friendly fire avoided from " + shooter + " to " + c);
				return false;
			}
		return true;
	}
}
