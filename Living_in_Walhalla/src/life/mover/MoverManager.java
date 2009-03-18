package life.mover;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import life.IMover;
import life.munition.IBullet;

public class MoverManager {

	protected static final int MOVERS = 1;
	protected LinkedList<MoverThread> moverThreads;

	protected static final int BULLETS = 2;
	protected LinkedList<MoverThread> bulletThreads;

	protected Semaphore moverThreadsLocker = new Semaphore(0);

	public void start() {
		// moverThreads
	}

	public void askToStop() {
		System.out.println("dying MoverManager");
		for (MoverThread m : moverThreads)
			m.askToStop();
	}

	public MoverManager(int timeStep, int nbThread) {
		super();
		MoverThread m;
		this.moverThreads = new LinkedList<MoverThread>();
		this.bulletThreads = new LinkedList<MoverThread>();
		for (int i = 3; i < nbThread; i++) {
			moverThreads.add(m = new MoverThread(timeStep, MOVERS));
			m.start();
			m.setPriority(Thread.MIN_PRIORITY);
		}
		for (int i = 0; i < 3; i++) {
			bulletThreads.add(m = new MoverThread(timeStep, BULLETS));
			m.start();
			m.setPriority(Thread.NORM_PRIORITY);
		}
		moverThreadsLocker.release();
	}

	public void addMovers(IMover movers) {
		moverThreadsLocker.acquireUninterruptibly();
		if (movers instanceof IBullet) {
			MoverThread m = bulletThreads.removeFirst();
			m.addMovers(movers);
			bulletThreads.addLast(m);
		} else {
			MoverThread m = moverThreads.removeFirst();
			m.addMovers(movers);
			moverThreads.addLast(m);
		}
		moverThreadsLocker.release();
	}

	protected void iHaveLostSomeMovers(MoverThread mt) {
		moverThreadsLocker.acquireUninterruptibly();
		MoverThread m;
		switch (mt.getFamilly()) {
		case BULLETS:
			m = moverThreads.getFirst();
			moverThreads.remove(mt);
			moverThreads.addFirst(mt);
			if (mt.movers.size() > m.movers.size()) {
				moverThreads.removeFirst();
				moverThreads.addLast(m);
			}
			break;
		case MOVERS:
			m = moverThreads.getFirst();
			moverThreads.remove(mt);
			moverThreads.addFirst(mt);
			if (mt.movers.size() > m.movers.size()) {
				moverThreads.removeFirst();
				moverThreads.addLast(m);
			}
			break;
		default:
			break;
		}
		moverThreadsLocker.release();
	}
}
