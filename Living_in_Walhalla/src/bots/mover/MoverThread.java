package bots.mover;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import bots.IMover;

public class MoverThread extends Thread {

	protected int timeStep;
	protected boolean dontKillMe = true;
	protected LinkedList<IMover> movers;
	protected Semaphore verrou = new Semaphore(1);

	@Override
	public void run() {
		long l;
		System.out.println("starting MoverManager");
		while (dontKillMe) {
			l = System.currentTimeMillis();
			verrou.acquireUninterruptibly();
			for (IMover m : movers)
				m.moveInItsDirection();
			verrou.release();
			try {
				l += timeStep - System.currentTimeMillis();
				if (l > 3)
					Thread.sleep(l);
				// else
				// System.out.println("Too Much");
			} catch (InterruptedException e) {
			}
		}
		System.out.println("dying MoverManager");
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