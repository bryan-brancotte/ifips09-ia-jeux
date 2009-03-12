package life.mover;

import java.util.LinkedList;
import java.util.concurrent.Semaphore;

import life.IMover;
import life.munition.IBullet;

public class MoverManager {

	protected LinkedList<MoverThread> moverThreads;

	protected MoverThread bulletThread;

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
		// TODO faire un groupe de thread pour les balles et non un seul thread
		for (int i = 1; i < nbThread; i++) {
			moverThreads.add(m = new MoverThread(timeStep));
			m.start();
			m.setPriority(Thread.MIN_PRIORITY);
		}
		bulletThread = new MoverThread(timeStep);
		bulletThread.start();
		bulletThread.setPriority(Thread.MIN_PRIORITY);
		moverThreadsLocker.release();
	}

	public void addMovers(IMover movers) {
		moverThreadsLocker.acquireUninterruptibly();
		if (movers instanceof IBullet) {
			bulletThread.addMovers(movers);
		} else {
			MoverThread m = moverThreads.removeFirst();
			m.addMovers(movers);
			moverThreads.addLast(m);
		}
		moverThreadsLocker.release();
	}

	protected void iHaveLostSomeMovers(MoverThread mt) {
		moverThreadsLocker.acquireUninterruptibly();
		if (mt != bulletThread) {
			MoverThread m = moverThreads.getFirst();
			moverThreads.remove(mt);
			moverThreads.addFirst(mt);
			if (mt.movers.size() > m.movers.size()) {
				moverThreads.removeFirst();
				moverThreads.addLast(m);
			}
		}
		moverThreadsLocker.release();
	}
}
