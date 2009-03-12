package life;

import java.awt.Color;

public interface ITeam {

	public String getName();

	// public void addPlayer(ICharacter character);

	public boolean isOpposedTo(ITeam team);

	public Color getColor();

}
