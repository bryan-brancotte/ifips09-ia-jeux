package aStar;

import utils.LIFO_Pool.Iterator;

public class AStarMultiThread<T> {

	protected AStarComutingThread<T> computing;

	public AStarMultiThread() {
		super();
		computing = new AStarComutingThread<T>(true);
		computing.start();
	}

	/**
	 * demande l'arrêt du thread de A*, sinon il continue d'exister et empèche votre application de se finir
	 */
	public void askToStop() {
		computing.askToStop();
	}

	/**
	 * Lance le calcul du prochain chemin
	 * 
	 * @param origin
	 * @param destination
	 */
	public void computPath(Node<T> origin, Node<T> destination) {
		computing.setWork(origin, destination);
	}

	/**
	 * retourne le dernier chemin calculé
	 * 
	 * @return
	 */
	public Iterator<T> getLastPath() {
		return computing.getLastPath();
	}

	public float getCostLastPath() {
		return computing.getCostLastPath();
	}

	public static void main(String args[]) throws InterruptedException {
		// Node<String> A = new Node<String>("A");
		// Node<String> B = new Node<String>("B");
		// Node<String> C = new Node<String>("C");
		// Node<String> D = new Node<String>("D");
		// Node<String> E = new Node<String>("E");
		// linkNeighborString(A, B, 1);
		// linkNeighborString(A, E, 2);
		// linkNeighborString(E, D, 11);
		// linkNeighborString(B, C, 10);
		// linkNeighborString(C, D, 1);
		// AStar<String> aStar = new AStar<String>();
		// LIFO<String> path = aStar.findPath(A, D);
		// Iterator<String> it = path.iterator();
		// while (it.hasNext())
		// System.out.println(it.next());
		Node<Dot> A = new Node<Dot>(new MyDot("A", 1, 1));
		Node<Dot> B = new Node<Dot>(new MyDot("B", 2, 1));
		Node<Dot> C = new Node<Dot>(new MyDot("C", 2, 2));
		Node<Dot> D = new Node<Dot>(new MyDot("D", 4, 2));
		Node<Dot> E = new Node<Dot>(new MyDot("E", 4, 1));
		Node<Dot> F = new Node<Dot>(new MyDot("F", 4, 3));
		linkNeighborDot(A, B);
		linkNeighborDot(A, C);
		linkNeighborDot(B, C);
		linkNeighborDot(C, D);
		linkNeighborDot(D, E);
		linkNeighborDot(A, F);
		linkNeighborDot(F, D);
		AStarMultiThread<Dot> aStar = new AStarMultiThread<Dot>();
		Iterator<Dot> it;


		aStar.computPath(A, E);
		Thread.sleep(50);
		it = aStar.getLastPath();
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());

		aStar.computPath(A, E);
		Thread.sleep(50);
		it = aStar.getLastPath();
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());
		aStar.askToStop();
	}

	public static void linkNeighborString(Node<String> A, Node<String> B, float cost) {
		A.addNeighbor(B, cost);
		B.addNeighbor(A, cost);
	}

	public static void linkNeighborDot(Node<Dot> A, Node<Dot> B) {
		A.addNeighbor(B, Node.getDistanceFrom(A.getOrigin(), B.getOrigin()));
		B.addNeighbor(A, Node.getDistanceFrom(A.getOrigin(), B.getOrigin()));
	}
}
