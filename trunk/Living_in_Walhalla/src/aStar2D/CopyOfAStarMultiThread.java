package aStar2D;

import utils.LIFO_Pool.Iterator;

public class CopyOfAStarMultiThread {

	protected CopyOfAStarComutingThread computing;

	public CopyOfAStarMultiThread() {
		this(true);
	}

	/**
	 * 
	 * @param useHeuristique
	 */
	public CopyOfAStarMultiThread(boolean useHeuristique) {
		super();
		computing = new CopyOfAStarComutingThread(useHeuristique);
		computing.start();
	}

	public void askToStop() {
		computing.askToStop();
	}

	public void computPath(Node origin, Node destination) {
		if ((origin == null) || (destination == null))
			return;
		computing.setWork(origin, destination);
	}

	public Iterator<Node> getLastPath() {
		return computing.getLastPath();
	}

	public boolean isWorking() {
		return !computing.isInterrupted();
	}

	public float getCostLastPath() {
		return computing.getCostLastPath();
	}

	public float getTimeLastPath() {
		return computing.getTimeLastPath();
	}

	public Node getDestinationLastPath() {
		return computing.getDestinationDone();
	}

	public static void main(String args[]) {
		Node A = new Node(1, 1);
		Node B = new Node(2, 1);
		Node C = new Node(2, 2);
		Node D = new Node(4, 2);
		Node E = new Node(4, 1);
		Node F = new Node(4, 3);
		Iterator<Node> it;
		Node.linkNode(A, B);
		Node.linkNode(A, C);
		Node.linkNode(B, C);
		Node.linkNode(C, D);
		Node.linkNode(D, E);
		Node.linkNode(A, F);
		Node.linkNode(F, D);
		CopyOfAStarMultiThread aStar = new CopyOfAStarMultiThread();

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		aStar.computPath(A, E);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		it = aStar.getLastPath();
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		aStar.computPath(A, E);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		it = aStar.getLastPath();
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());
		aStar.askToStop();

	}
}
