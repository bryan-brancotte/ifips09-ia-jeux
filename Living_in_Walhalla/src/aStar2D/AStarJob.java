package aStar2D;

import java.util.concurrent.Semaphore;

import life.IMover;

import utils.LIFO_Pool;
import utils.LIFO_Pool.Iterator;

public class AStarJob {

	protected Node origin;
	protected Node destination;
	protected LIFO_Pool<Node> chemin;
	protected long lastTimeDone = 0;
	protected float cost = 0;
	protected IMover owner;
	public IMover getOwner() {
		return owner;
	}

	protected Semaphore verrou = new Semaphore(1);

	protected AStarJob(IMover owner) {
		chemin = new LIFO_Pool<Node>();
		this.owner = owner;
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
		return ((System.nanoTime() - lastTimeDone) > 2e9);
	}
}
