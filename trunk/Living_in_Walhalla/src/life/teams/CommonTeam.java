package life.teams;

import java.awt.Color;
import java.awt.Graphics;

import utils.Vector2d;

import aStar2D.Node;

import life.ICharacter;
import life.IMover;
import life.ITeam;

public class CommonTeam implements ITeam {

	private String name;
	private Color color;

	public CommonTeam(String name, Color color) {
		super();
		this.name = name;
		this.color = color;
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
	}

	@Override
	public int draw(Graphics g, int x, int y, int heigth) {
		return 0;
	}

	@Override
	public void attack(Node target, int qte) {
	}

	@Override
	public void newOrders() {
	}

	@Override
	public int getCountFighter() {
		return 0;
	}

	@Override
	public boolean isNotFriendlyFire(IMover shooter, Vector2d dest) {
		return true;
	}

}
