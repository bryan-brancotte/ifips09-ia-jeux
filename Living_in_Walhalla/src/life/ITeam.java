package life;

import java.awt.Color;
import java.awt.Graphics;

import utils.Vector2d;

import aStar2D.Node;

public interface ITeam {

	public abstract int draw(Graphics g, int x, int y, int heigth);

	public String getName();

	public void registerPlayer(ICharacter character);

	public boolean isOpposedTo(ITeam team);

	public Color getColor();

	public void newOrders();

	public void attack(Node target, int qte);

	public int getCountFighter();

	public boolean isNotFriendlyFire(IMover shooter, Vector2d dest);

}
