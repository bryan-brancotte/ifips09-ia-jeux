package life.bots;

import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import life.ICharacter;
import life.ITeam;

public class FightingTeam implements ITeam {

	private String name;
	private Color color;
	protected ArrayList<ICharacter> players;

	public FightingTeam(String name, Color color) {
		super();
		this.name = name;
		this.color = color;
		this.players = new ArrayList<ICharacter>(8);
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
}
