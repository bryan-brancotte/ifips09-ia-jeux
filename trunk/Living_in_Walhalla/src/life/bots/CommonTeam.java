package life.bots;

import java.awt.Color;

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

}
