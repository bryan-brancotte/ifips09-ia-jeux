package life.strategies;

import life.IMover;
import life.IStrategie;
import life.ITeam;
import life.bots.Cow;
import utils.CodeExecutor;
import utils.LIFO.Iterator;
import aStar2D.Node;
import aStar2D.Node.Link;
import applets.BattleField;

public class OnAtATimeStrategie extends Thread implements IStrategie {
	protected static boolean dontKillMe = true;
	protected static final int TIME_REEVAL_SITUATION = 500;

	protected BattleField battleField;
	protected ITeam myTeam;
	protected IMover target = null;

	public OnAtATimeStrategie(BattleField battleField) {
		super();
		this.battleField = battleField;
		this.start();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		while (dontKillMe) {
			try {
				while (myTeam == null)
					Thread.currentThread().suspend();

				if (target == null || target.isDead()) {
					battleField.iterateOnMoverToDraw(new CodeExecutor<IMover>() {

						@Override
						public void execute(IMover param) {
							if (param.getTeam().isOpposedTo(myTeam) && param.getTeam().isOpposedTo(Cow.nature))
								target = param;
						}

						@Override
						public boolean keepIterat() {
							return false;
						}

						@Override
						public void endingExecution() {
						}
					});
				}
				if (target != null)
					myTeam.attack(target.getNode(), -1);
				Thread.sleep(TIME_REEVAL_SITUATION);
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
	@SuppressWarnings("unused")
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
		myTeam = team;
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
