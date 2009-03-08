package aStar2D;

import java.util.concurrent.Semaphore;

import utils.LIFO_Pool;
import utils.LIFO_Pool.Iterator;

public class AStarJob {

	protected Node origin;
	protected Node destination;
	protected LIFO_Pool<Node> chemin;
	protected long lastTimeDone = 0;
	protected float cost = 0;
	protected Semaphore verrou = new Semaphore(1);

	protected AStarJob() {
		chemin = new LIFO_Pool<Node>();
	}

	public Node getDestination() {
		return destination;
	}

	public Iterator<Node> getPath() {
		verrou.acquireUninterruptibly();
		verrou.release();
		return chemin.iterator();
	}

	public boolean needRebuild() {
		return ((System.nanoTime() - lastTimeDone) > 2e7);
	}
}
