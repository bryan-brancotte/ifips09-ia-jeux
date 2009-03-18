package life;

import java.awt.Color;
import java.awt.Graphics;

public interface ITeam {

	public abstract int draw(Graphics g, int x, int y, int heigth);

	public String getName();

	public void registerPlayer(ICharacter character);

	public boolean isOpposedTo(ITeam team);

	public Color getColor();

}
