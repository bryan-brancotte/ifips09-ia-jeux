package bots;

import java.awt.Color;
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

public class Personnage implements IBot, IMover {

	public static int MAX_DST = 40;
	protected AStarMultiThread aStar;

	protected BattleField battleField;

	protected Vector2d coord;

	protected String name;

	protected Node destination = null;

	protected Node direction = null;

	protected Node node = null;

	protected LinkedList<Node> nodes = null;

	protected Color myColor;

	protected HashSet<Node> nodesBuffer = null;
	protected final Semaphore verrou = new Semaphore(1);

	public Personnage(BattleField battleField, AStarMultiThread aStar, Color color, String name) {
		super();
		this.battleField = battleField;
		this.aStar = aStar;
		this.myColor = color;
		this.name = name;
		coord = new Vector2d(10, 10);
		nodes = new LinkedList<Node>();
		nodesBuffer = new HashSet<Node>();
		node = null;
	}

	@Override
	public void AI() {
		// TODO Auto-generated method stub

	}

	@Override
	public float botRadius() {
		return 3;
	}

	@Override
	public void draw(Graphics g) {
		if (coord == null)
			return;
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(myColor);
		g.drawLine((int) (x - botRadius()), y, (int) (x + botRadius()), y);
		g.drawLine(x, (int) (y - botRadius()), x, (int) (y + botRadius()));
		g.drawOval((int) (coord.x - botRadius()), (int) (coord.y - botRadius()), (int) (botRadius() * 2),
				(int) (botRadius() * 2));
		g.setColor(Color.black);
		// for (Node n : nodes)
		// g.drawOval((int) (n.x - botRadius()*2), (int) (n.y - botRadius()*2),
		// (int) (botRadius() * 4),
		// (int) (botRadius() * 4));
	}

	@Override
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

	public float getSpeed() {
		return 0.5F;
	}

	public void move(float dx, float dy) {
		coord.x += dx;
		coord.y += dy;
		updatePosition();
	}

	public void move(Vector2d d) {
		coord.add(d);
		updatePosition();
	}

	@Override
	public boolean moveInItsDirection() {
		if (node == destination) {
			//System.out.println(name + " is done");
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

	public void moveTo(Vector2d d) {
		coord.set(d);
		nodes.clear();
		updatePosition();
	}

	public void setDestination(Node destination) {
		this.destination = destination;
	}

	@Override
	public void updatePosition() {
		if (!verrou.tryAcquire())
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
			if (costTmp > MAX_DST || !battleField.surface.canSee(coord, n))
				nodesBuffer.add(n);
			// else
			// System.out.println(costTmp);
		}
		for (Node n : nodesBuffer)
			nodes.remove(n);
		nodesBuffer.clear();
		verrou.release();
	}
}
