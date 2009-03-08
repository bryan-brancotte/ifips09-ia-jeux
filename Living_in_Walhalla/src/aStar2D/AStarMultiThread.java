package aStar2D;

import java.util.HashMap;

import utils.LIFO_Pool.Iterator;

public class AStarMultiThread {

	protected AStarComutingThread computing;

	protected HashMap<Object, AStarJob> jobs;

	/**
	 * 
	 * @param useHeuristique
	 */
	public AStarMultiThread(boolean useHeuristique) {
		super();
		computing = new AStarComutingThread(useHeuristique);
		computing.start();
		jobs = new HashMap<Object, AStarJob>();
	}

	public void askToStop() {
		computing.askToStop();
	}

	public void computPath(Node origin, Node destination, Object forWho) {
		if ((origin == null) || (destination == null))
			return;
		AStarJob job = jobs.get(forWho);
		if (job == null) {
			job = new AStarJob();
			jobs.put(forWho, job);
		}
		if (job.origin != origin || job.destination != destination) {
			job.origin = origin;
			job.destination = destination;
			computing.setWork(job);
		} else if (job.needRebuild())
			computing.setWork(job);
	}

	public Iterator<Node> getPath(Object forWho) {
		AStarJob job = jobs.get(forWho);
		if (job == null) {
			job = new AStarJob();
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
