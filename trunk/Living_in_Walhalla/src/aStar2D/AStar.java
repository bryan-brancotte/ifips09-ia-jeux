package aStar2D;

import aStar2D.Node.Link;
import utils.LIFO_Pool;
import utils.SortedList;
import utils.LIFO_Pool.Iterator;

public class AStar {

	protected SortedList<Node> frontiere;

	protected LIFO_Pool<Node> chemin;

	protected float cost;

	public AStar() {
		super();
		frontiere = new SortedList<Node>();
		chemin = new LIFO_Pool<Node>();
	}

	public Iterator<Node> computPath(Node origin, Node destination) {
		Node activeDot = null;
		utils.LIFO.Iterator<Link> it;
		Node.Link voisin;
		frontiere.removeAll(false);
		frontiere.add(origin, 0);

		// tan que la frontière n'est pas vide, et que le node au coût le plus faible n'est pas la destination
		while (frontiere.hasNext() && ((activeDot = frontiere.removeFirst()) != destination)) {
			// on parcourt les voisin du noeud au plus faible coût
//			System.out.println(frontiere.size());
			it = activeDot.getNeighbor();
			// on marque ce noeud pour qu'on ne le considère plus comme un point de passage
			activeDot.setFinished(true);
			// tan qu'il y a des voisins
			while ((voisin = it.next()) != null)
				// si ce n'est pas un voisin qui est dans l'espace connu
				if (!voisin.node.isFinished())
					// on ajout tente d'ajoute se noeud à la frontière, la SortedList peut refusé de l'ajouter si le
					// noeud est déja présent avec un coût inférieur
					if (frontiere.add(voisin.node, activeDot.getPreviousCost() + voisin.cost
							+ voisin.node.heuristique(destination)))
						// on définit ca ce voisin que le noeud précédent est le noeud actif
						voisin.node.setPrevious(activeDot.getPreviousCost() + voisin.cost, activeDot);

		}
		// on a finit, et on a peut-être trouver un chemin

		// si on en a trouvé un
		if (activeDot == destination) {
			do {
				// on ajouter les stations à la liste
				chemin.add(activeDot);
			} while ((activeDot = activeDot.getPreviousNode()) != null);
			cost = destination.getPreviousCost();
		} else
			cost = Float.POSITIVE_INFINITY;
		// on retourne la liste, elle sera vide si on n'a pas trouvé de chemin.
		return chemin.iterator();
	}

	public void cleanPath(){
		chemin.removeAll(false);
	}
	public Iterator<Node> getLastPath() {
		return chemin.iterator();
	}

	public float getCostLastPath() {
		return cost;
	}

	public static void main(String args[]) {
		Node A = new Node(1, 1);
		Node B = new Node(2, 1);
		Node C = new Node(2, 2);
		Node D = new Node(4, 2);
		Node E = new Node(4, 1);
		Node F = new Node(4, 3);
		Node.linkNode(A, B);
		Node.linkNode(A, C);
		Node.linkNode(B, C);
		Node.linkNode(C, D);
		Node.linkNode(D, E);
		Node.linkNode(A, F);
		Node.linkNode(F, D);
		AStar aStar = new AStar();
		Iterator<Node> it = aStar.computPath(A, E);
		System.out.println("Cost = " + aStar.getCostLastPath());
		while (it.hasNext())
			System.out.println(it.next());

	}
}
