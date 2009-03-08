package aStar2D;

import java.util.concurrent.Semaphore;

import utils.LIFO;
import utils.LIFO_Pool;
import utils.SortedList;
import utils.LIFO_Pool.Iterator;
import aStar2D.Node.Link;

public class CopyOfAStarComutingThread extends Thread {

	private SortedList<Node> frontiere;

	private LIFO_Pool<Node> chemin;

	private float cost;
	private float time;
	private Node origin;
	private Node destination;
	private Node destinationDone;

	private boolean killRunningComputing = false;

	private boolean runningComputing = false;

	private boolean askToDie = false;

	private Semaphore cheminLocker = new Semaphore(1);

	private Semaphore newWorkIncomming = new Semaphore(1);

	private boolean useHeuristique;

	protected CopyOfAStarComutingThread(boolean useHeuristique) {
		super();
		this.useHeuristique = useHeuristique;
		frontiere = new SortedList<Node>();
		chemin = new LIFO_Pool<Node>();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		long beg;
		System.out.println("starting AStarComutingThread");
		while (!askToDie) {
			while (runningComputing) {
				// System.out.println("Job to do");
				newWorkIncomming.acquireUninterruptibly();
				beg = System.nanoTime();
				runningComputing = false;
				killRunningComputing = false;
				Node.resetTmpInfo();
				computPath(origin, destination);
				time = (System.nanoTime() - beg) * 1e-6F;
				newWorkIncomming.acquireUninterruptibly();
				newWorkIncomming.release();
				// System.out.println("Job done");
			}
			newWorkIncomming.release();
			this.suspend();
		}
		System.out.println("dying AStarComutingThread");
	}

	private void computPath(Node origin, Node destination) {
		// System.out.println("Realease   in run1");
		newWorkIncomming.release();
		Node activeDot = null;
		LIFO.Iterator<Link> it;
		Node.Link voisin;
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
					if (frontiere.add(voisin.node, activeDot.getPreviousCost() + voisin.cost + heur))
						// on définit ca ce voisin que le noeud précédent est le
						// noeud actif
						voisin.node.setPrevious(activeDot.getPreviousCost() + voisin.cost, activeDot);
				}
			}

		}
		// on a finit, et on a peut-être trouver un chemin

		cheminLocker.acquireUninterruptibly();
		chemin.removeAll(false);
		// si on en a trouvé un
		if (activeDot == destination) {
			destinationDone = destination;
			int i = 0;
			do {
				// on ajouter les stations à la liste
				chemin.add(activeDot);
				i++;
				// System.out.println(activeDot);
			} while ((activeDot = activeDot.getPreviousNode()) != null && i < Node.sizeWorld());
			if (runningComputing = i >= Node.sizeWorld())
				System.out.println("Pb?");
			;
			cost = destination.getPreviousCost();
		} else
			cost = Float.POSITIVE_INFINITY;
		cheminLocker.release();
	}

	public Node getDestinationDone() {
		return destinationDone;
	}

	public Iterator<Node> getLastPath() {
		Iterator<Node> ret;
		try {
			cheminLocker.acquire();
			ret = chemin.iterator();
			cheminLocker.release();
		} catch (InterruptedException e) {
			System.err
					.println("Impossible de vérouiller la verrou cheminLocker, l'iterateur retourné ne contient rien et c'est anormal");
			ret = (new LIFO_Pool<Node>()).iterator();
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
	public void setWork(Node origin, Node destination) {
		newWorkIncomming.acquireUninterruptibly();
		this.origin = origin;
		this.destination = destination;
		if (origin != destination) {
			// if (this.runningComputing)
			// this.killRunningComputing = true;
			this.runningComputing = true;
			this.resume();
		} else {
			this.time = 0;
			this.cost = 0;
		}
		newWorkIncomming.release();
	}

	@SuppressWarnings("deprecation")
	public void setWorkNotProtected(Node origin, Node destination) {
		this.origin = origin;
		this.destination = destination;
		if (this.runningComputing)
			this.killRunningComputing = true;
		this.runningComputing = true;
		this.resume();
	}

	@SuppressWarnings("deprecation")
	public void askToStop() {
		askToDie = true;
		this.resume();
	}
}
