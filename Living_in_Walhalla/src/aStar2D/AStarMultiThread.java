package aStar2D;

import java.util.HashMap;

import utils.LIFO_Pool.Iterator;
import bots.IMover;

public class AStarMultiThread {

	protected AStarComutingThread computing;

	protected HashMap<IMover, AStarJob> jobs;

	/**
	 * 
	 * @param useHeuristique
	 */
	public AStarMultiThread(boolean useHeuristique) {
		super();
		computing = new AStarComutingThread(useHeuristique);
		computing.start();
		jobs = new HashMap<IMover, AStarJob>();
	}

	public void askToStop() {
		computing.askToStop();
	}

	public void computPath(Node origin, Node destination, IMover forWho) {
		if ((origin == null) || (destination == null))
			return;
		AStarJob job = jobs.get(forWho);
		if (job == null) {
			job = new AStarJob(forWho);
			jobs.put(forWho, job);
		}
		if (job.origin != origin || job.destination != destination) {
			job.origin = origin;
			job.destination = destination;
			computing.setWork(job);
		} else if (job.needRebuild())
			computing.setWork(job);
	}

	public Iterator<Node> getPath(IMover forWho) {
		AStarJob job = jobs.get(forWho);
		if (job == null) {
			job = new AStarJob(forWho);
			jobs.put(forWho, job);
		}
		return job.getPath();
	}

	public boolean isWorking() {
		return !computing.isInterrupted();
	}

	public float getCostPath(Object forWho) {
		return jobs.get(forWho).cost;
	}
}
