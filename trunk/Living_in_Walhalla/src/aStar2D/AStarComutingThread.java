package aStar2D;

import java.util.LinkedHashSet;
import java.util.concurrent.Semaphore;

import utils.LIFO;
import utils.SortedList;
import aStar2D.Node.Link;

public class AStarComutingThread extends Thread {

	private SortedList<Node> frontiere;

	// private AStarJob job;

	private LinkedHashSet<AStarJob> jobs;

	// private boolean killRunningComputing = false;

	// private boolean runningComputing = false;

	private boolean askToDie = false;

	// private Semaphore cheminLocker = new Semaphore(1);

	private Semaphore newWorkIncomming = new Semaphore(1);

	private boolean useHeuristique;

	protected AStarComutingThread(boolean useHeuristique) {
		super();
		this.useHeuristique = useHeuristique;
		frontiere = new SortedList<Node>();
		jobs = new LinkedHashSet<AStarJob>();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		// long l;
		System.out.println("starting AStarComutingThread");
		while (!askToDie) {
			while (!this.jobs.isEmpty()) {
				// System.out.println("Job to do : " + jobs.size());
				newWorkIncomming.acquireUninterruptibly();
				// runningComputing = false;
				// killRunningComputing = false;
				Node.resetTmpInfo();
				computPath(jobs.iterator().next());
				newWorkIncomming.acquireUninterruptibly();
				newWorkIncomming.release();
				// System.out.println("Job done");
			}
			// System.out.println("Going to sleep");
			// newWorkIncomming.release();
			// l = System.nanoTime();
			this.suspend();
			// System.out.println((int) ((System.nanoTime() - l) * 1e-6));
			// System.out.println("Waking up");
		}
		System.out.println("dying AStarComutingThread");
	}

	private void computPath(AStarJob job) {
		// System.out.println("Realease   in run1");
		jobs.remove(job);
		newWorkIncomming.release();
		Node activeDot = null;
		LIFO.Iterator<Link> it;
		Node.Link voisin;
		// int max = 0, tmp;
		float heur = 0F;
		frontiere.removeAll(false);
		frontiere.add(job.origin, 0);

		// tan que la frontière n'est pas vide, et que le node au coût le plus
		// faible n'est pas la destination
		while (frontiere.hasNext() && ((activeDot = frontiere.removeFirst()) != job.destination)) {
			// if (killRunningComputing)
			// return;
			// on parcourt les voisin du noeud au plus faible coût
			it = activeDot.getNeighbor();
			// on marque ce noeud pour qu'on ne le considère plus comme un point
			// de passage
			activeDot.setFinished(true);
			// tan qu'il y a des voisins
			while ((voisin = it.next()) != null) {
				// if (killRunningComputing)
				// return;
				// System.out.println(max = (((tmp = frontiere.size()) > max) ?
				// tmp : max));
				// si ce n'est pas un voisin qui est dans l'espace connu
				if (!voisin.node.isFinished()) {
					// on ajout tente d'ajoute se noeud à la frontière, la
					// SortedList peut refusé de l'ajouter si le
					// noeud est déja présent avec un coût inférieur
					if (useHeuristique)
						heur = voisin.node.heuristique(job.destination);
					if (frontiere.add(voisin.node, activeDot.getPreviousCost() + activeDot.getOverCost(job.getOwner())
							+ voisin.cost + heur))
						// on définit ca ce voisin que le noeud précédent est le
						// noeud actif
						voisin.node.setPrevious(activeDot.getPreviousCost() + activeDot.getOverCost(job.getOwner())
								+ voisin.cost, activeDot);
				}
			}

		}
		// on a finit, et on a peut-être trouver un chemin

		// cheminLocker.acquireUninterruptibly();
		job.verrou.acquireUninterruptibly();
		job.chemin.removeAll(false);
		// si on en a trouvé un
		if (activeDot == job.destination) {
			int i = 0;
			do {
				// on ajouter les stations à la liste
				job.chemin.add(activeDot);
				i++;
				// System.out.println(activeDot);
			} while ((activeDot = activeDot.getPreviousNode()) != null && i < Node.sizeWorld());
			if (i >= Node.sizeWorld()) {
				System.out.println("Pb?");
				newWorkIncomming.acquireUninterruptibly();
				jobs.add(job);
				newWorkIncomming.release();
			}
			job.cost = job.destination.getPreviousCost();
		} else
			job.cost = Float.POSITIVE_INFINITY;
		job.lastTimeDone = System.nanoTime();
		job.verrou.release();
		// cheminLocker.release();
	}

	@SuppressWarnings("deprecation")
	public void setWork(AStarJob job) {
		newWorkIncomming.acquireUninterruptibly();
		if (job.origin != job.destination) {
			// System.out.println(jobs.size());
			this.jobs.add(job);
			// if (this.runningComputing)
			// this.killRunningComputing = true;
			// this.runningComputing = true;
			this.resume();
		} else {
			job.cost = 0;
		}
		newWorkIncomming.release();
	}

	@SuppressWarnings("deprecation")
	public void askToStop() {
		askToDie = true;
		this.resume();
	}
}
