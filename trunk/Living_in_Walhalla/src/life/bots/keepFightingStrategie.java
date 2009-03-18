package life.bots;

import java.util.ArrayList;

import life.ICharacter;
import life.IStrategie;
import life.ITeam;
import applets.BattleField;

public class keepFightingStrategie extends Thread implements IStrategie {
	protected static boolean dontKillMe = true;

	protected ArrayList<ICharacter> players;
	protected BattleField battleField;
	protected FightingTeam myTeam;

	public keepFightingStrategie(BattleField battleField) {
		super();
		this.battleField = battleField;
		this.players = new ArrayList<ICharacter>(8);
		this.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long l;
		// System.out.println("starting MoverThread");
		while (dontKillMe) {
			l = System.currentTimeMillis();
			try {
				while (myTeam == null)
					Thread.currentThread().suspend();
				System.out.println("keepFightingStrategie for " + myTeam.getName());
				l += 4e3 - System.currentTimeMillis();
				// selon les mesure, le temps d'endormir et reveillez un thread
				// coute une 1 ms
				if (l > 2)
					Thread.sleep(l);
			} catch (InterruptedException e) {
			}
		}
		// System.out.println("dying MoverThread");
	}

	public void askToStop() {
		dontKillMe = false;
	}

	@SuppressWarnings("deprecation")
	@Override
	public void worksFor(ITeam team) {
		if (team instanceof FightingTeam)
			myTeam = (FightingTeam) team;
		this.resume();

	}

}
