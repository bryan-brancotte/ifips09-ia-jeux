package bots;

import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import utils.Vector2d;
import aStar2D.AStarMultiThread;
import aStar2D.Node;
import aStar2D.Node.Link;
import applets.BattleField;

public abstract class ICharacter implements IMover {

	public static int MAX_DST = 100;
	protected AStarMultiThread aStar;

	protected BattleField battleField;

	protected Vector2d coord;

	protected Node destination = null;

	protected Node direction = null;

	protected Node node = null;

	protected LinkedList<Node> nodes = null;
	protected Semaphore nodesLocker = new Semaphore(1);

	protected HashSet<Node> nodesBuffer = null;

	private boolean journeyDone = false;

	protected ITeam team;

	public ITeam getTeam() {
		return team;
	}

	public ICharacter(BattleField battleField, AStarMultiThread aStar, Node startupPosition, ITeam team) {
		super();
		this.battleField = battleField;
		this.aStar = aStar;
		this.team = team;
		coord = new Vector2d(startupPosition.x, startupPosition.y);
		nodes = new LinkedList<Node>();
		nodesBuffer = new HashSet<Node>();
		node = null;
	}

	public abstract String getName();

	public abstract float botRadius();

	public abstract void drawCharacter(Graphics g);

	public void draw(Graphics g) {
		// nodesLocker.acquireUninterruptibly();
		// for (Node n : nodes) {
		// g.drawOval((int) n.x - 2, (int) (n.y - 2), 4, 4);
		// }
		// nodesLocker.release();
		drawCharacter(g);
	}

	public Vector2d getCoord() {
		return coord;
	}

	public Node getDestination() {
		return destination;
	}

	public Node getNode() {
		return node;
	}

	public Iterator<Node> getNodes() {
		return nodes.iterator();
	}

	public int getNodesCount() {
		return nodes.size();
	}

	public abstract float getSpeed();

	public void move(float dx, float dy) {
		coord.x += dx;
		coord.y += dy;
		updatePosition();
	}

	public void move(Vector2d d) {
		coord.add(d);
		updatePosition();
	}

	protected abstract void journeyDone();

	@Override
	public boolean moveInItsDirection() {
		if (node == destination) {
			if (!journeyDone) {
				journeyDone = true;
				journeyDone();
				// System.out.println(this.getName() + " is done");
			}
			return true;
		}
		aStar.computPath(node, destination, this);
		// System.out.println(node);
		boolean directionDone = false;
		Node n, np;
		utils.LIFO_Pool.Iterator<Node> itPath = aStar.getPath(this);
		direction = null;
		np = itPath.next();
		while (!directionDone && itPath.hasNext()) {
			n = itPath.next();
			// System.out.println(coord + " " + node + " " + n);
			if (np != null) {
				directionDone = true;
				// System.out.println(direction + " " + n);
				direction = n;
			}
		}
		// System.out.println(destination+ " : " + direction + " from " + node);
		if (direction == null)
			return false;
		Vector2d v = direction.subtract(coord);
		float norme;
		if ((norme = v.norme()) > getSpeed())
			v.setScale(getSpeed() / norme, v);
		coord.setSum(v, coord);
		updatePosition();
		return norme < getSpeed() / 10;
	}

	/**
	 * Téléport le personnage à cette localisation
	 */
	public void moveTo(Vector2d d) {
		coord.set(d);
		nodes.clear();
		updatePosition();
	}

	/**
	 * Définit la destination comme étant le node passé en param
	 * 
	 * @param destination
	 */
	public void setDestination(Node destination) {
		journeyDone = false;
		this.destination = destination;
	}

	public void updatePosition() {
		if (!nodesLocker.tryAcquire())
			return;
		if (nodes.size() == 0) {
			for (Node n : battleField.getWaypoint())
				nodes.add(n);
			node = null;
		} else {
			utils.LIFO.Iterator<Link> itN;
			Node tmp;
			for (Node n : nodes) {
				itN = n.getNeighbor();
				while (itN.hasNext()) {
					tmp = itN.next().getNode();
					if (!nodesBuffer.contains(tmp) && !nodes.contains(tmp))
						nodesBuffer.add(tmp);
				}
			}
			for (Node n : nodesBuffer)
				nodes.add(n);
			nodesBuffer.clear();
		}
		if (node == null)
			node = nodes.getFirst();
		float costTmp;
		float cost = node.distance(coord);
		for (Node n : nodes) {
			if ((costTmp = n.distance(coord)) < cost) {
				node = n;
				cost = costTmp;
			}
			if (costTmp > MAX_DST || !battleField.surface.canSee(coord, n)) {
				nodesBuffer.add(n);
				n.setInfluence(this, 0);
			} else {
				costTmp -= MAX_DST;
				n.setInfluence(this, -costTmp);
			}
		}
		for (Node n : nodesBuffer)
			nodes.remove(n);
		nodesBuffer.clear();
		nodesLocker.release();
	}

	@Override
	public String toString() {
		return getName();
	}
}
