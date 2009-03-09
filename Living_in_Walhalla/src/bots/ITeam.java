package bots;

public interface ITeam {

	public String getName();

	// public void addPlayer(ICharacter character);

	public boolean isOpposedTo(ITeam team);

}
