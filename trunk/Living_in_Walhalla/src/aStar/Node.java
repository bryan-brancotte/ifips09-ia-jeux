package aStar;

import utils.LIFO;
import utils.LIFO_Pool;
import utils.LIFO_Pool.Iterator;

public class Node<T> {

	protected static int neighborGeneral = 0;

	protected static LIFO<Node<?>> world;

	public class Link {
		protected float cost;
		protected Node<T> node;

		public Link(float cost, Node<T> node) {
			super();
			this.cost = cost;
			this.node = node;
		}

		protected Link() {
			super();
			this.cost = 0;
			this.node = null;
		}

		protected void set(float cost, Node<T> node) {
			this.cost = cost;
			this.node = node;
		}

		public float getCost() {
			return cost;
		}

		public Node<T> getNode() {
			return node;
		}
	}

	protected boolean finished = false;

	protected LIFO_Pool<Link> neighbor;

	protected T origin;

	protected Link previous = null;

	public Node(T origin) {
		super();
		if (world == null)
			world = new LIFO<Node<?>>();
		world.add(this);
		this.previous = new Link();
		this.origin = origin;
		this.neighbor = new LIFO_Pool<Link>();
	}

	public void addNeighbor(Node<T> dot, float cost) {
		neighbor.add(new Link(cost, dot));
	}

	public static void resetTmpInfo() {
		if (world == null)
			return;
		utils.LIFO.Iterator<Node<?>> it = world.iterator();
		Node<?> n;
		while (it.hasNext()) {
			n = it.next();
			n.setFinished(false);
			n.setPrevious(0, null);
		}
	}

	public Iterator<Link> getNeighbor() {
		return neighbor.iterator();
	}

	public T getOrigin() {
		return origin;
	}

	public float getPreviousCost() {
		if (previous == null)
			return 0;
		return previous.cost;
	}

	public Node<T> getPreviousNode() {
		if (previous == null)
			return null;
		return previous.node;
	}

	public boolean isFinished() {
		return finished;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setPrevious(float cost, Node<T> node) {
		this.previous.set(cost, node);
	}

	public float heuristique(Node<?> node) {
		if ((node.origin instanceof Dot) && (this.origin instanceof Dot)) {
			return getDistanceFrom((Dot) node.origin, (Dot) this.origin);
		}
		return 0;
	}

	public static final float getDistanceFrom(Dot dot1, Dot dot2) {
		float ret;
		float val = dot1.getX() - dot2.getX();
		ret = val * val;
		val = dot1.getY() - dot2.getY();
		ret += val * val;
		if (dot2.is3d() && dot1.is3d()) {
			val = dot1.getZ() - dot2.getZ();
			ret += val * val;
		}
		// utilisation de la racine carré de John Carmack révisé par Chris Lomont
		// Pour John Carmack : 0x5f3759df
		// Pour Chris Lomont : 0x5f375a86
		// les temps de calcul se retourve divisé par 2
		int i;
		val = ret * 0.5F;
		i = Float.floatToRawIntBits(ret);
		i = 0x5f375a86 - (i >> 1);
		ret = Float.intBitsToFloat(i);
		ret = ret * (1.5F - (val * ret * ret));

		return (1.0F / ret);
	}

	// public static float squareRootFloat(float n) {
	// int i;
	// float x2, y;
	// x2 = n * 0.5F;
	// y = n;
	// i = Float.floatToRawIntBits(y);
	// i = 0x5f375a86 - (i >> 1);
	// y = Float.intBitsToFloat(i);
	// y = y * (1.5F - (x2 * y * y));
	//
	// return (long) (1.0F / y);
	// }
}
