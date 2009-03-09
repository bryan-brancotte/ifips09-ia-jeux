package bots.mover;

import java.util.LinkedList;

public class MoverManager {

	protected LinkedList<MoverThread> moverThreads;

	public void start() {
		// moverThreads
	}

	public void askToStop() {
		for (MoverThread m : moverThreads)
			m.askToStop();
	}

	public MoverManager(int timeStep, int nbThread) {
		super();
		MoverThread m;
		this.moverThreads = new LinkedList<MoverThread>();
		for (int i = 0; i < nbThread; i++) {
			moverThreads.add(m = new MoverThread(timeStep));
			m.start();
			m.setPriority(Thread.MIN_PRIORITY);
		}
	}

	public void addMovers(IMover movers) {
		MoverThread m = moverThreads.removeFirst();
		m.addMovers(movers);
		moverThreads.addLast(m);
	}

}
