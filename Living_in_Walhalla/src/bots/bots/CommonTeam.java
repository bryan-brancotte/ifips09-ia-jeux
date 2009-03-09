package bots.bots;

import bots.ITeam;

public class CommonTeam implements ITeam {

	private String name;

	public  CommonTeam(String name) {
		super();
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public boolean isOpposedTo(ITeam team) {
		return team != this;
	}

}
