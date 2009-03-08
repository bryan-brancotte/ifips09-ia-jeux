package bots;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

public class MoverManager extends Thread {

	protected int timeStep;
	protected boolean dontKillMe = true;
	protected LinkedList<IMover> movers;
	protected Semaphore verrou = new Semaphore(1);

	@Override
	public void run() {
		System.out.println("starting MoverManager");
		while (dontKillMe) {
			verrou.acquireUninterruptibly();
			for (IMover m : movers)
				m.moveInItsDirection();
			verrou.release();
			try {
				Thread.sleep(timeStep);
			} catch (InterruptedException e) {
			}
		}
		System.out.println("dying MoverManager");
	}

	public void askToStop() {
		dontKillMe = false;
	}

	public MoverManager(int timeStep) {
		super();
		this.movers = new LinkedList<IMover>();
		this.timeStep = timeStep;
	}

	public void addMovers(IMover movers) {
		verrou.acquireUninterruptibly();
		this.movers.add(movers);
		verrou.release();
	}

}
