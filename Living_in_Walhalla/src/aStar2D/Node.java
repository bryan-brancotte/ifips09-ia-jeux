package aStar2D;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.concurrent.Semaphore;

import bots.IMover;
import utils.LIFO;
import utils.Vector2d;
import utils.LIFO.Iterator;

public class Node extends Vector2d {

	protected static LIFO<Node> world;

	protected HashMap<IMover, Float> influentials;
	protected Semaphore influentialsLocker = new Semaphore(1);

	public class Link {
		protected float cost;
		protected Node node;

		protected Link() {
			super();
			this.cost = 0;
			this.node = null;
		}

		public Link(float cost, Node node) {
			super();
			this.cost = cost;
			this.node = node;
		}

		public float getCost() {
			return cost;
		}

		public Node getNode() {
			return node;
		}

		protected void set(float cost, Node node) {
			this.cost = cost;
			this.node = node;
		}
	}

	protected boolean finished = false;

	protected LIFO<Link> neighbor;

	protected Link previous = null;

	public Node() {
		super();
		init();
	}

	public Node(float x, float y) {
		super(x + 0.1F, y + 0.1F);
		init();
	}

	private void init() {
		if (world == null)
			world = new LIFO<Node>();
		influentials = new HashMap<IMover, Float>();
		world.add(this);
		this.previous = new Link();
		this.neighbor = new LIFO<Link>();
	}

	public void setInfluence(IMover mover, float influence) {
		influentialsLocker.acquireUninterruptibly();
		if (influence == 0)
			influentials.remove(mover);
		else
			influentials.put(mover, influence);
		influentialsLocker.release();
	}

	public static void resetTmpInfo() {
		if (world == null)
			return;
		Iterator<Node> it = world.iterator();
		Node n;
		while (it.hasNext()) {
			n = it.next();
			n.setFinished(false);
			n.setPrevious(0, null);
		}
	}

	public static int sizeWorld() {
		return world.getSize();
	}

	public void addNeighbor(Node dot, float cost) {
		neighbor.add(new Link(cost, dot));
	}

	public void removeNeighbor(Node dot) {
		Iterator<Link> it = neighbor.iterator();
		Link l;
		while ((l = it.next()) != null) {
			if (l.node == dot)
				neighbor.remove(l);
		}
	}

	public void removeAllNeighbor() {
		neighbor.removeAll();
	}

	public LIFO.Iterator<Link> getNeighbor() {
		return neighbor.iterator();
	}

	public int getNeighborCount() {
		return neighbor.getSize();
	}

	public float getPreviousCost() {
		if (previous == null)
			return 0;
		return previous.cost;
	}

	public Node getPreviousNode() {
		if (previous == null)
			return null;
		return previous.node;
	}

	public float heuristique(Vector2d node) {
		return 0;
		// return node.distance(node);
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setPrevious(float cost, Node node) {
		this.previous.set(cost, node);
	}

	public float getOverCost(IMover forWho) {
		float oc = 0F;
		influentialsLocker.acquireUninterruptibly();
		for (Entry<IMover, Float> e : influentials.entrySet()) {
			if (e.getKey().getTeam().isOpposedTo(forWho.getTeam()))
				oc += e.getValue();
			else if (forWho != e.getKey())
				oc -= 5;
		}
		influentialsLocker.release();
		return oc;
	}

	public static void linkNode(Node A, Node B) {
		// if ((A == null) || (B == null))
		// return;
		float cost = A.distance(B);
		A.addNeighbor(B, cost);
		B.addNeighbor(A, cost);
	}
}
