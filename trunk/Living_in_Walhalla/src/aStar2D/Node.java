package aStar2D;

import bots.IMover;
import utils.LIFO;
import utils.Vector2d;
import utils.LIFO.Iterator;

public class Node extends Vector2d {

	protected static LIFO<Node> world;

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
		if (world == null)
			world = new LIFO<Node>();
		world.add(this);
		this.previous = new Link();
		this.neighbor = new LIFO<Link>();
	}

	public Node(float x, float y) {
		super(x + 0.1F, y + 0.1F);
		if (world == null)
			world = new LIFO<Node>();
		world.add(this);
		this.previous = new Link();
		this.neighbor = new LIFO<Link>();
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
		return 0;
	}

	public static void linkNode(Node A, Node B) {
		// if ((A == null) || (B == null))
		// return;
		float cost = A.distance(B);
		A.addNeighbor(B, cost);
		B.addNeighbor(A, cost);
	}
}