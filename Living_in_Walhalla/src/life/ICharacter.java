package life;

import java.awt.Color;
import java.awt.Graphics;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import life.munition.IBullet;
import utils.Vector2d;
import aStar2D.AStarMultiThread;
import aStar2D.Node;
import aStar2D.Node.Link;
import applets.BattleField;

public abstract class ICharacter implements IMover {

	public static int MAX_DST = 100;
	protected static AStarMultiThread aStar;
	protected static BattleField battleField;

	protected Vector2d coord;
	protected Node destination = null;
	protected Node direction = null;
	protected Node node = null;
	protected LinkedList<Node> nodes = null;
	protected Semaphore nodesLocker = new Semaphore(1);
	protected ICharacter me = this;

	protected HashSet<Node> nodesBuffer = null;

	private boolean journeyDone = false;

	protected ITeam team;
	protected int life;

	public int getLife() {
		return life;
	}

	public abstract int getInitialLife();

	protected String name;
	protected boolean iAmDead = false;
	private long nextPossibleShoot = System.nanoTime() + 2000000000;
	private boolean canShoot = true;

	public ITeam getTeam() {
		return team;
	}

	public static void init(BattleField battleField, AStarMultiThread aStar) {
		ICharacter.battleField = battleField;
		ICharacter.aStar = aStar;
	}

	public ICharacter(Node startupPosition, ITeam team, String name) {
		super();
		this.team = team;
		coord = new Vector2d(startupPosition.x, startupPosition.y);
		nodes = new LinkedList<Node>();
		nodesBuffer = new HashSet<Node>();
		node = null;
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public abstract void drawCharacter(Graphics g);

	public void draw(Graphics g) {
		// nodesLocker.acquireUninterruptibly();
		// for (Node n : nodes) {
		// g.drawOval((int) n.x - 2, (int) (n.y - 2), 4, 4);
		// }
		// nodesLocker.release();
		// g.drawOval((int) coord.x - MAX_DST, (int) (coord.y - MAX_DST),
		// MAX_DST << 1, MAX_DST << 1);
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
		try {
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
			// System.out.println(destination+ " : " + direction + " from " +
			// node);
			if (direction == null)
				return false;
			Vector2d v = direction.subtract(coord);
			float norme;
			if ((norme = v.norme()) > getSpeed())
				v.setScale(getSpeed() / norme, v);
			coord.setSum(v, coord);
			updatePosition();
			return norme < getSpeed() / 10;
		} finally {
			if (canShoot || (canShoot = (System.nanoTime() - nextPossibleShoot > 0)))
				canShoot();
		}
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

	protected abstract void canShoot();

	protected void fire(IBullet munition) {
		// System.out.println(name + " tire vers\t" + (int) target.x + "\t" +
		// (int) target.y);
		if (!canShoot)
			return;
		canShoot = false;
		nextPossibleShoot = System.nanoTime() + munition.reloadingTime();
		battleField.addMover(munition);// new Bullet(5, coord, target));
	}

	@Override
	public String toString() {
		return getName();
	}

	@Override
	public boolean isDead() {
		return iAmDead;
	}

	@Override
	public void hit(int damage) {
		iAmDead |= ((life -= damage) < 0);
		if (iAmDead) {
			System.out.println(this.getName() + " is dead !");
			for (Node n : nodes)
				n.setInfluence(this, 0);
		}
	}

	@Override
	public Color getColor() {
		return team.getColor();
	}
}
