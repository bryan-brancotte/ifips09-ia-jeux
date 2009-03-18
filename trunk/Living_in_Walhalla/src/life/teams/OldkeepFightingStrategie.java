package life.teams;

import life.ICharacter;
import life.IStrategie;
import life.ITeam;
import utils.LIFO.Iterator;
import aStar2D.Node;
import aStar2D.Node.Link;
import applets.BattleField;

public class OldkeepFightingStrategie extends Thread implements IStrategie {
	protected static boolean dontKillMe = true;
	protected static final int TIME_REEVAL_SITUATION = 10000;

	protected BattleField battleField;
	protected FightingTeam myTeam;
	NodeTarget[] targets;
	NodeTarget targetBuffer;
	NodeTarget targetWriting;

	public OldkeepFightingStrategie(BattleField battleField) {
		super();
		this.battleField = battleField;
		this.start();
		targets = new NodeTarget[battleField.TAILLE_TEAM];
		for (int cpt = 0; cpt < targets.length; cpt++)
			targets[cpt] = new NodeTarget();
		targetWriting = new NodeTarget();
		targetBuffer = new NodeTarget();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		int tmp;
		int cpt;
		while (dontKillMe) {
			try {
				while (myTeam == null)
					Thread.currentThread().suspend();

				if (myTeam.players.size() != 0) {
					if (targets.length != myTeam.players.size()) {
						targets = new NodeTarget[myTeam.players.size()];
						for (cpt = 0; cpt < targets.length; cpt++)
							targets[cpt] = new NodeTarget();
					} else {
						for (cpt = 0; cpt < targets.length; cpt++)
							targets[cpt].cost = Integer.MIN_VALUE;
					}
					for (Node n : battleField.getWaypoint()) {
						tmp = heuristiqueSuicide(n);
						cpt = targets.length;
						while (cpt > 0 && targets[cpt - 1].cost < tmp) {
							cpt--;
						}
						if (cpt < targets.length) {
							targetWriting.n = n;
							targetWriting.cost = tmp;
							for (; cpt < targets.length; cpt++) {
								targetBuffer.n = targets[cpt].n;
								targetBuffer.cost = targets[cpt].cost;
								targets[cpt].n = targetWriting.n;
								targets[cpt].cost = targetWriting.cost;
								targetWriting.n = targetBuffer.n;
								targetWriting.cost = targetBuffer.cost;
							}
						}
					}
					cpt = 0;
					System.out.println("Target for " + myTeam.getName() + " :\t" + targets[0].n + " with "
							+ targets[0].cost + "\twhois " + targets[0].n.getOverCost(myTeam));
					for (ICharacter c : myTeam.players) {
						c.setDestination(targets[cpt++].n);
					}
					Thread.sleep(TIME_REEVAL_SITUATION);
				} else {
					Thread.sleep(100);
				}
			} catch (InterruptedException e) {
			}
		}
	}

	/**
	 * heuristique sensé donner les bonne embuscade
	 * 
	 * @param centralNode
	 * @return
	 */
	@SuppressWarnings("unused")
	private int heuristiqueGoodSpot(Node centralNode) {
		float ret = 0;
		float overCost = centralNode.getOverCost(myTeam);
		int cpt = 0;
		// ret *= -ret;
		// if (ret > 0)
		// return 0;
		Iterator<Link> it = centralNode.getNeighbor();
		while (it.hasNext()) {
			ret += it.next().getNode().getOverCost(myTeam) - overCost;
			cpt++;
		}
		return (int) ret * cpt;
	}

	/**
	 * heuristique qui mène au coeur du dangé
	 * 
	 * @param centralNode
	 * @return
	 */
	private int heuristiqueSuicide(Node centralNode) {
		float ret = centralNode.getOverCost(myTeam);
		Iterator<Link> it = centralNode.getNeighbor();
		while (it.hasNext()) {
			ret += it.next().getNode().getOverCost(myTeam);
		}
		return (int) ret;
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

	protected class NodeTarget {
		public int cost;
		public Node n;

		protected NodeTarget() {
			n = null;
		}
	}

}
