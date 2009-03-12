package life.mover;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import life.IMover;

public class MoverThread extends Thread {

	protected int timeStep;
	protected int familly;
	protected boolean dontKillMe = true;
	protected LinkedList<IMover> movers;
	protected Semaphore verrou = new Semaphore(1);
	protected IMover itsDead = null;

	@Override
	public void run() {
		long l;
		// System.out.println("starting MoverThread");
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
			try {
				l += timeStep - System.currentTimeMillis();
				// selon les mesure, le temps d'endormir et reveillez un thread
				// coute une 1 ms
				if (l > 2)
					Thread.sleep(l);
			} catch (InterruptedException e) {
			}
		}
		// System.out.println("dying MoverThread");
	}

	protected void askToStop() {
		dontKillMe = false;
	}

	protected MoverThread(int timeStep, int familly) {
		super();
		this.movers = new LinkedList<IMover>();
		this.timeStep = timeStep;
		this.familly = familly;
	}

	public int getFamilly() {
		return familly;
	}

	protected void addMovers(IMover movers) {
		verrou.acquireUninterruptibly();
		this.movers.add(movers);
		verrou.release();
	}
}