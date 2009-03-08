package aStar;

import utils.LIFO;
import utils.LIFO_Pool;
import utils.SortedList;
import utils.LIFO.Iterator;

public class AStar<T> {

	protected SortedList<Node<T>> frontiere;

	protected LIFO<T> chemin;

	protected float cost;

	private boolean useHeuristique;

	public AStar(boolean useHeuristique) {
		super();
		this.useHeuristique = useHeuristique;
		frontiere = new SortedList<Node<T>>();
		chemin = new LIFO<T>();
	}

	public AStar() {
		this(true);
	}

	private Iterator<T> computPath(Node<T> origin, Node<T> destination) {
		Node.resetTmpInfo();
		// System.out.println("Realease in run1");
		Node<T> activeDot = null;
		LIFO_Pool.Iterator<Node<T>.Link> it;
		Node<T>.Link voisin;
		// int max = 0, tmp;
		float heur = 0F;
		frontiere.removeAll(false);
		frontiere.add(origin, 0);

		// tan que la frontière n'est pas vide, et que le node au coût le plus faible n'est pas la destination
		while (frontiere.hasNext() && ((activeDot = frontiere.removeFirst()) != destination)) {
			// on parcourt les voisin du noeud au plus faible coût
			it = activeDot.getNeighbor();
			// on marque ce noeud pour qu'on ne le considère plus comme un point de passage
			activeDot.setFinished(true);
			// tan qu'il y a des voisins
			while ((voisin = it.next()) != null) {
				// System.out.println(max = (((tmp = frontiere.size()) > max) ? tmp : max));
				// si ce n'est pas un voisin qui est dans l'espace connu
				if (!voisin.node.isFinished()) {
					// on ajout tente d'ajoute se noeud à la frontière, la SortedList peut refusé de l'ajouter si le
					// noeud est déja présent avec un coût inférieur
					if (useHeuristique)
						heur = voisin.node.heuristique(destination);
					if (frontiere.add(voisin.node, activeDot.getPreviousCost() + voisin.cost + heur))
						// on définit ca ce voisin que le noeud précédent est le noeud actif
						voisin.node.setPrevious(activeDot.getPreviousCost() + voisin.cost, activeDot);
				}
			}

		}
		// on a finit, et on a peut-être trouver un chemin

		// si on en a trouvé un
		if (activeDot == destination) {
			do {
				// on ajouter les stations à la liste
				chemin.add(activeDot.getOrigin());
				// System.out.println(activeDot);
			} while ((activeDot = activeDot.getPreviousNode()) != null);
			cost = destination.getPreviousCost();
		} else
			cost = Float.POSITIVE_INFINITY;
		// on retourne la liste, elle sera vide si on n'a pas trouvé de chemin.
		return chemin.iterator();
	}

	public Iterator<T> getLastPath() {
		return chemin.iterator();
	}

	public float getCostLastPath() {
		return cost;
	}

	public static void main(String args[]) {
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
		AStar<Dot> aStar = new AStar<Dot>();
		Iterator<Dot> it;
		it = aStar.computPath(A, E);
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());
		it = aStar.computPath(A, E);
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());

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
