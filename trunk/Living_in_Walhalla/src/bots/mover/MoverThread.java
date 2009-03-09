package bots.mover;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import bots.IMover;

public class MoverThread extends Thread {

	protected int timeStep;
	protected boolean dontKillMe = true;
	protected LinkedList<IMover> movers;
	protected Semaphore verrou = new Semaphore(1);
	protected IMover itsDead = null;
	static long ok = 0;
	static long all = 0;

	@Override
	public void run() {
		long l;
		System.out.println("starting MoverThread");
		while (dontKillMe) {
			l = System.currentTimeMillis();
			verrou.acquireUninterruptibly();
			for (IMover m : movers) {
				if (m.isDead())
					itsDead = m;
				else
					m.moveInItsDirection();
			}
			if (itsDead != null) {
				movers.remove(itsDead);
				itsDead = null;
			}
			verrou.release();
			all++;
			try {
				l += timeStep - System.currentTimeMillis();
				// selon les mesure, le temps d'endormir et reveillez un thread
				// coute une 1 ms
				if (l > 2) {
					ok++;
					Thread.sleep(l);
				}
				if (all % 100 == 0)
					System.out.println(ok + " / " + all + "% : " + (ok * 100F / all) + "%");
			} catch (InterruptedException e) {
			}
		}
		System.out.println("dying MoverThread");
	}

	protected void askToStop() {
		dontKillMe = false;
	}

	protected MoverThread(int timeStep) {
		super();
		this.movers = new LinkedList<IMover>();
		this.timeStep = timeStep;
	}

	protected void addMovers(IMover movers) {
		verrou.acquireUninterruptibly();
		this.movers.add(movers);
		verrou.release();
	}
}