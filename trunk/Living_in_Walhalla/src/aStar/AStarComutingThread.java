package aStar;

import java.util.concurrent.Semaphore;

import utils.LIFO_Pool;
import utils.SortedList;
import utils.LIFO_Pool.Iterator;

public class AStarComutingThread<T> extends Thread {

	private SortedList<Node<T>> frontiere;

	private LIFO_Pool<T> chemin;

	private float cost = Float.NaN;
	private float time;
	private Node<T> origin;
	private Node<T> destination;

	private boolean killRunningComputing = false;

	private boolean RunningComputing = false;

	private boolean askToDie = false;

	private Semaphore cheminLocker = new Semaphore(1);

	private Semaphore newWorkIncomming = new Semaphore(1);

	private boolean useHeuristique;

	protected AStarComutingThread(boolean useHeuristique) {
		super();
		this.useHeuristique = useHeuristique;
		frontiere = new SortedList<Node<T>>();
		chemin = new LIFO_Pool<T>();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long beg;
		System.out.println("starting");
		while (!askToDie) {
			while (RunningComputing) {
				System.out.println("Computing");
				newWorkIncomming.acquireUninterruptibly();
				beg = System.nanoTime();
				RunningComputing = false;
				killRunningComputing = false;
				Node.resetTmpInfo();
				computPath(origin, destination);
				time = (System.nanoTime() - beg) * 1e-6F;
				System.out.println("done");
				newWorkIncomming.acquireUninterruptibly();
				newWorkIncomming.release();
			}
			newWorkIncomming.release();
			System.out.println("Going to sleep");
			this.suspend();
		}
		System.out.println("Dying");
	}

	private void computPath(Node<T> origin, Node<T> destination) {
		Node.resetTmpInfo();
		newWorkIncomming.release();
		Node<T> activeDot = null;
		LIFO_Pool.Iterator<Node<T>.Link> it;
		Node<T>.Link voisin;
		// int max = 0, tmp;
		float heur = 0F;
		frontiere.removeAll(false);
		frontiere.add(origin, 0);

		// tan que la frontière n'est pas vide, et que le node au coût le plus
		// faible n'est pas la destination
		while (frontiere.hasNext() && ((activeDot = frontiere.removeFirst()) != destination)) {
			if (killRunningComputing)
				return;
			// on parcourt les voisin du noeud au plus faible coût
			it = activeDot.getNeighbor();
			// on marque ce noeud pour qu'on ne le considère plus comme un point
			// de passage
			activeDot.setFinished(true);
			// tan qu'il y a des voisins
			while ((voisin = it.next()) != null) {
				if (killRunningComputing)
					return;
				// System.out.println(max = (((tmp = frontiere.size()) > max) ?
				// tmp : max));
				// si ce n'est pas un voisin qui est dans l'espace connu
				if (!voisin.node.isFinished()) {
					// on ajout tente d'ajoute se noeud à la frontière, la
					// SortedList peut refusé de l'ajouter si le
					// noeud est déja présent avec un coût inférieur
					if (useHeuristique)
						heur = voisin.node.heuristique(destination);
					if (frontiere.add(voisin.node, activeDot.getPreviousCost() + activeDot.getOverCost() + voisin.cost
							+ heur))
						// on définit ca ce voisin que le noeud précédent est le
						// noeud actif
						voisin.node.setPrevious(activeDot.getPreviousCost() + activeDot.getOverCost() + voisin.cost,
								activeDot);
				}
			}

		}
		// on a finit, et on a peut-être trouver un chemin

		try {
			cheminLocker.acquire();
			chemin.removeAll(false);
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
			cheminLocker.release();
		} catch (InterruptedException e) {
			System.err.println("Impossible de vérouiller la verrou cheminLocker, le chemin n'a pas été mit à jours");
		}
		// on retourne la liste, elle sera vide si on n'a pas trouvé de chemin.
	}

	public Iterator<T> getLastPath() {
		Iterator<T> ret;
		try {
			cheminLocker.acquire();
			ret = chemin.iterator();
			cheminLocker.release();
		} catch (InterruptedException e) {
			System.err
					.println("Impossible de vérouiller la verrou cheminLocker, l'iterateur retourné ne contient rien et c'est anormal");
			ret = (new LIFO_Pool<T>()).iterator();
		}
		return ret;
	}

	public float getCostLastPath() {
		return cost;
	}

	public float getTimeLastPath() {
		return time;
	}

	@SuppressWarnings("deprecation")
	public void setWork(Node<T> origin, Node<T> destination) {
		newWorkIncomming.acquireUninterruptibly();
		this.origin = origin;
		this.destination = destination;
		if (this.RunningComputing)
			this.killRunningComputing = true;
		this.RunningComputing = true;
		this.resume();
		newWorkIncomming.release();
	}

	@SuppressWarnings("deprecation")
	public void askToStop() {
		this.killRunningComputing = true;
		this.RunningComputing = false;
		askToDie = true;
		this.resume();
	}
}
