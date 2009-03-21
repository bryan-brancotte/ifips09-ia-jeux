package applets;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ConcurrentModificationException;
import java.util.LinkedList;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

import life.ICharacter;
import life.IMover;
import life.ITeam;
import life.bots.Cow;
import life.bots.Fantassin;
import life.bots.Personnage;
import life.bots.Rabbit;
import life.bots.Tank;
import life.mover.MoverManager;
import life.munition.Bullet;
import life.munition.IBullet;
import life.munition.Obus;
import life.strategies.OnAtATimeStrategie;
import life.strategies.keepFightingStrategie;
import life.teams.FightingTeam;
import surface.Surface;
import utils.CodeExecutor;
import utils.Vector2d;
import utils.LIFO.Iterator;
import waypoint.WaypointInstaller;
import aStar2D.AStarMultiThread;
import aStar2D.Node;

/**
 * Very simple applet to handle fundamentals of A.I. in games.
 * 
 * This is the main applet, it handles a "battle field" with objects on it,
 * Every frames the whole field is paint again, there is a (simple) GUI.
 * 
 * How it works? After initialization of Surface, Bots, ... the applet enters
 * (in run()) an infinite loop that... just sleep to wait for next frame, and
 * then call updateBots(), then updateBelettes() then repaint(). The first and
 * second calls update positions and animations of bots and bullets, the last
 * one simple repaint all the field from scratch.
 * 
 * You may want to extend this applet using openGL like JOGL in order to enter
 * the third dimension A very simple entry for this would be for instance
 * http://jsolutions.se/DukeBeanEm
 * 
 * @author L. Simon, Univ. Paris Sud, 2008
 * 
 */

public class BattleField extends Applet implements Runnable, MouseListener, MouseMotionListener {
	// Those constants are hard constants... Why? I don't know.
	static final public float MAXX = 10000F; // Size of the battlefield, in
	// float (not pixels)

	static final public float MAXY = 7500F;

	static final public int PREF_VIEWER_XSIZE = 800;
	// size in pixels (in x, the y is automatically deduced)
	private static final long serialVersionUID = 1L;
	protected BattleFieldBehavior comportement = BattleFieldBehavior.MOVER;
	protected final int DRAW_PATH = 0;
	protected final boolean DRAW_WAYPOINT = false;
	protected final boolean DRAW_CONTROL_MAP = false;
	protected final boolean LEVEL_CREATING = false;
	public final int TAILLE_TEAM = 8;
	protected int levelCreatingCpt = 0;

	protected MoverManager moverManager;

	protected long lastTimePaint = 0;

	public static void main(String args[]) {
		Frame f = new Frame();
		BattleField app = new BattleField();
		app.init();
		app.start();
		app.stop();
		f.add("Center", app);
	}

	/**
	 * Where to draw (off-screen buffer)
	 */
	Graphics buffer_canvas;
	/**
	 * Canvas for double buffering
	 */
	Image buffer_canvasimage;
	/**
	 * string printed in the simple hud. For debugging...
	 */
	String gui_string = "";

	// Two point2D to memorize mouse gestures (pointA first click, pointB second
	// click)
	private Node pointOrigin;
	private Node pointDestination;
	private Personnage perso;
	private Personnage perso2;

	/**
	 * 
	 */
	private ITeam teamRed;
	private ITeam teamBlue;
	private Semaphore moverToDrawLocker = new Semaphore(1);
	private Semaphore bulletToDrawLocker = new Semaphore(1);
	private Object started = moverToDrawLocker;
	private LinkedList<IMover> moverToDraw;
	private LinkedList<IMover> bulletToDraw;
	// private Collection<IMover> moverToDrawSafe;

	public Surface surface; // The surface that contains the objects...
	Node[] waypoints;
	AStarMultiThread aStar;

	/**
	 * 
	 */
	private Thread update; // Thread that sleeps and update the screen.
	Graphics viewer_canvas; // What the user actually see (on-screen buffer)
	float viewer_scale; // Ratio from size of surface to size of viewer
	int viewer_xsize;
	int viewer_ysize;

	// Very simple constructor
	public BattleField() {
		viewer_scale = MAXX / PREF_VIEWER_XSIZE;
	}

	/**
	 * Very simple GUI.. Just print the infos string on the bottom of the
	 * screen, in a rectangle.
	 */
	private void drawHUD() {
		teamBlue.draw(buffer_canvas, 3, viewer_ysize - 23, 20);
		teamRed.draw(buffer_canvas, 3, viewer_ysize - 46, 20);
		// buffer_canvas.setColor(Color.red);
		// buffer_canvas.drawRect(20, viewer_ysize - 23, viewer_xsize - 41, 20);
		// buffer_canvas.drawChars(gui_string.toCharArray(), 0, Math.min(80,
		// gui_string.length()), 22, viewer_ysize - 7);
	}

	public boolean handleEvent(Event event) {
		boolean returnValue = false;
		return returnValue;
	}

	public void init() {
		super.init();

		viewer_xsize = PREF_VIEWER_XSIZE; // size in pixels
		viewer_ysize = (int) (MAXY / viewer_scale); // The y axe is
		// automatically computed

		resize(viewer_xsize, viewer_ysize);
		buffer_canvasimage = createImage(viewer_xsize, viewer_ysize);
		buffer_canvas = buffer_canvasimage.getGraphics();
		((Graphics2D) buffer_canvas).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
				RenderingHints.VALUE_ANTIALIAS_ON);
		viewer_canvas = this.getGraphics();

		addMouseListener(this);
		addMouseMotionListener(this);

		initSurface();
		initWaypoint();
		initaStar();
		initBots();
		initBelettes();
		startBots();
		try {
			Thread.sleep(200);
		} catch (InterruptedException e) {
		}
		started = null;
	}

	/**
	 * Called ones to init all your belettes structures.
	 */
	public void initBelettes() {
		Bullet.init(this);
		Obus.init(this);
		// addMoverUnsafe(new Bullet(5, new Vector2d(5, 5), new Vector2d(5,
		// 400)));
		// addMoverUnsafe(new Bullet(5, new Vector2d(5, 300), new Vector2d(5,
		// 400)));
	}

	/**
	 * Called ones to init all your bots.
	 */
	public void initBots() {
		teamBlue = new FightingTeam("Blue", Color.blue, new OnAtATimeStrategie(this));
		teamRed = new FightingTeam("Red", Color.red, new keepFightingStrategie(this));
		moverToDraw = new LinkedList<IMover>();
		bulletToDraw = new LinkedList<IMover>();
		IMover im;
		ICharacter ic;
		ICharacter.init(this, aStar);

		moverManager = new MoverManager(30, 10);
		moverManager.start();

		perso = new Personnage("Florence", teamRed);
		moverManager.addMovers(perso);
		perso.setDestination(waypoints[Cow.rand.nextInt(waypoints.length)]);
		perso2 = new Personnage("Bryan", teamBlue);
		perso2.setDestination(waypoints[Cow.rand.nextInt(waypoints.length)]);
		moverManager.addMovers(perso2);

		moverToDrawLocker.acquireUninterruptibly();
		for (int i = 1; i <= TAILLE_TEAM; i++) {
			ic = new Fantassin(new Node((TAILLE_TEAM - i + 1) * 10, 10 * i), teamRed, "Florence_" + i);
			moverManager.addMovers(ic);
			moverToDraw.add(ic);
			teamRed.registerPlayer(ic);
		}
		// for (int i = 1; i <= (TAILLE_TEAM >> 2); i++) {
		ic = new Tank(new Node(viewer_xsize - 10 * (TAILLE_TEAM - TAILLE_TEAM + 1), viewer_ysize - 10 * TAILLE_TEAM),
				teamBlue, "Bryan_" + TAILLE_TEAM);
		moverManager.addMovers(ic);
		moverToDraw.add(ic);
		teamBlue.registerPlayer(ic);
		// }
		for (int i = 2 + 1; i <= TAILLE_TEAM; i++) {
			ic = new Fantassin(new Node(viewer_xsize - 10 * (TAILLE_TEAM - i + 1), viewer_ysize - 10 * i), teamBlue,
					"Bryan_" + i);
			moverManager.addMovers(ic);
			moverToDraw.add(ic);
			teamBlue.registerPlayer(ic);
		}

		for (int i = 0; i < 1; i++) {
			moverManager.addMovers(im = new Cow(waypoints[Cow.rand.nextInt(waypoints.length)], waypoints));
			moverToDraw.add(im);
		}

		for (int i = 0; i < TAILLE_TEAM || i < 20; i++) {
			moverManager.addMovers(im = new Rabbit(waypoints[Cow.rand.nextInt(waypoints.length)], waypoints));
			moverToDraw.add(im);
		}
		moverToDrawLocker.release();
	}

	public void startBots() {
		moverToDrawLocker.acquireUninterruptibly();
		for (IMover im : moverToDraw)
			if (im instanceof ICharacter) {
				((ICharacter) im).updatePosition();
				// ((ICharacter)
				// im).setDestination(waypoints[Cow.rand.nextInt(waypoints.length)]);
			}
		perso.updatePosition();
		perso2.updatePosition();
		moverToDrawLocker.release();
	}

	/**
	 * Called ones to init the surface. This is where all objects attached to
	 * the surface should be loaded. Dynamic objects like bots and bullet are
	 * handled elsewhere.
	 */
	public void initSurface() {
		surface = new Surface(viewer_xsize, viewer_ysize, viewer_scale);
	}

	/**
	 * Called ones to creat the waypoint from the surface.
	 */
	public void initWaypoint() {
		waypoints = WaypointInstaller.placeWaypoint(surface);/**/
	}

	/**
	 * Called ones to creat the waypoint from the surface.
	 */
	public void initaStar() {
		aStar = new AStarMultiThread(false);
	}

	public Node[] getWaypoint() {
		return waypoints;
	}

	// Those methods have to be there... Even if they are empty.
	public void mouseClicked(MouseEvent e) {
	}

	public void mouseDragged(MouseEvent e) {
	}

	public void mouseEntered(MouseEvent e) {
	}

	public void mouseExited(MouseEvent e) {
	}

	public void mouseMoved(MouseEvent e) {
		if (comportement == BattleFieldBehavior.MOUSE_TRACKING) {
			// Node oldPointOrigin = perso.getNode();
			perso.move(e.getX() - perso.getCoord().x, e.getY() - perso.getCoord().y);
			// float cost = pointOrigin.distance(e.getX(), e.getY());
			// float costTmp;
			// for (int i = 1; i < waypoints.length; i++) {
			// if ((costTmp = waypoints[i].distance(e.getX(), e.getY())) < cost)
			// {
			// pointOrigin = waypoints[i];
			// cost = costTmp;
			// }
			// }
			// System.out.println("pointOrigin:" + pointOrigin + " at " + cost +
			// " from your clic");
			// System.out.println("pointOrigin:" + pointOrigin + " at " + cost +
			// " from your clic");
			// System.out.println(cpt);
			// if (oldPointOrigin != perso.getNode())/* ||/
			// &&(aStar.isWorking())/ */
			// /*if */(cpt++ % 50 == 0)) {
			// cpt = 1;
			// aStar.computPath(perso.getNode(), pointDestination);
			// }
		}
	}

	public void mousePressed(MouseEvent e) {
	}

	public void mouseReleased(MouseEvent e) {
		// System.out.println(comportement);
		if (comportement == BattleFieldBehavior.TWO_POINT_PATH) {
			if (waypoints == null || waypoints.length == 0)
				return;
			if ((pointDestination != null) || (pointOrigin == null)) {
				pointDestination = null;
				pointOrigin = waypoints[0];
				float cost = pointOrigin.distance(e.getX(), e.getY());
				float costTmp;
				for (int i = 1; i < waypoints.length; i++) {
					if ((costTmp = waypoints[i].distance(e.getX(), e.getY())) < cost) {
						pointOrigin = waypoints[i];
						cost = costTmp;
					}
				}
				// System.out.println("pointOrigin:" + pointOrigin + " at " +
				// cost + " from your clic");
			} else {
				pointDestination = waypoints[0];
				float cost = pointDestination.distance(e.getX(), e.getY());
				for (int i = 1; i < waypoints.length; i++) {
					waypoints[i].setFinished(false);
					waypoints[i].setPrevious(0, null);
					if (waypoints[i].distance(e.getX(), e.getY()) < cost) {
						pointDestination = waypoints[i];
						cost = pointDestination.distance(e.getX(), e.getY());
					}
				}
				// System.out.println("pointDestination:" + pointDestination +
				// " at " + cost + " from your clic");
				// aStar.computPath(pointOrigin, pointDestination);
			}
		} else if (comportement == BattleFieldBehavior.MOUSE_TRACKING || comportement == BattleFieldBehavior.MOVER) {
			pointDestination = waypoints[0];
			float cost = pointDestination.distance(e.getX(), e.getY());
			for (int i = 1; i < waypoints.length; i++) {
				waypoints[i].setFinished(false);
				waypoints[i].setPrevious(0, null);
				if (waypoints[i].distance(e.getX(), e.getY()) < cost) {
					pointDestination = waypoints[i];
					cost = pointDestination.distance(e.getX(), e.getY());
				}
			}
			if (e.getButton() == 1)
				perso.setDestination(pointDestination);
			else if (e.getButton() == 3)
				perso2.setDestination(pointDestination);
		}
		if (LEVEL_CREATING) {
			System.out.println("ob.addNode(new Vector2d(" + e.getX() + "F, " + e.getY() + "F));");
			if (++levelCreatingCpt == 4) {
				levelCreatingCpt = 0;
				System.out.println("ob.fixObject();");
				System.out.println("objects.add(ob);");
				System.out.println();
				System.out.println("ob = new PolylineObject(this);");
			}
		}
	}

	/*
	 * Called by repaint, to paint all the offscreen surface. We erase
	 * everything, then redraw each components.
	 * 
	 * @see java.awt.Container#paint(java.awt.Graphics)
	 */
	public void paint(Graphics g) {
		// 1. We erase everything
		buffer_canvas.setColor(Color.lightGray); // Background color
		buffer_canvas.fillRect(0, 0, viewer_xsize, viewer_ysize);

		// 2. We draw the surface (and its objects)
		surface.draw(buffer_canvas);
		buffer_canvas.setColor(Color.black);
		buffer_canvas.drawRect(0, 0, viewer_xsize - 1, viewer_ysize - 1);

		// Draw the Waypoint
		if (DRAW_WAYPOINT && waypoints != null) {
			Iterator<Node.Link> it;
			Node.Link wf;
			buffer_canvas.setColor(Color.yellow);
			for (Node w : waypoints) {
				drawDot(buffer_canvas, w, 4);
				it = w.getNeighbor();
				while ((wf = it.next()) != null) {
					buffer_canvas.drawLine((int) w.x, (int) w.y, (int) wf.getNode().x, (int) wf.getNode().y);
				}
			}
		}
		if (DRAW_CONTROL_MAP && waypoints != null) {
			// buffer_canvas.setColor(Color.yellow);
			int k;
			for (Node w : waypoints) {
				if ((k = (int) (1.3 * w.getOverCost(teamRed))) < 0) {
					if (k < -255)
						buffer_canvas.setColor(Color.red);
					else
						buffer_canvas.setColor(new Color(-k, 0, 0));
					drawDot(buffer_canvas, w, 4);
				} else if ((k = (int) (1.3 * w.getOverCost(teamBlue))) < 0) {
					if (k < -255)
						buffer_canvas.setColor(Color.blue);
					else
						buffer_canvas.setColor(new Color(0, 0, -k));
					drawDot(buffer_canvas, w, 4);
				} else if ((k = (int) (1.3 * w.getOverCost(Cow.nature))) < 0) {
					if (k < -255)
						buffer_canvas.setColor(Color.green);
					else
						buffer_canvas.setColor(new Color(0, -k, 0));
					drawDot(buffer_canvas, w, 4);
				}
			}
		}

		// Draw the Path
		if (DRAW_PATH > 0) {
			// 6. Draw the path
			switch (DRAW_PATH) {
			case 1:
				PathDrawing(perso);
				PathDrawing(perso2);
				break;
			case 2:
				if (3 >= moverToDraw.size())
					PathDrawing(moverToDraw.getLast());
				else
					PathDrawing(moverToDraw.get(3));
				if (3 + TAILLE_TEAM >= moverToDraw.size())
					PathDrawing(moverToDraw.getLast());
				else
					PathDrawing(moverToDraw.get(3 + TAILLE_TEAM));
				break;
			default:
				for (IMover im : moverToDraw)
					PathDrawing(im);
				break;
			}
		}
		gui_string = "[ FPS : " + (int) (1e9F / (System.nanoTime() - lastTimePaint)) + " ]";
		lastTimePaint = System.nanoTime();

		perso.draw(buffer_canvas);
		perso2.draw(buffer_canvas);

		// Draw the bots in their position/direction
		try {
			IMover moverDead = null;
			// if (
			moverToDrawLocker.tryAcquire(100, TimeUnit.MILLISECONDS)
			// ) {
			// moverToDrawLocker.acquireUninterruptibly()
			;
			for (IMover im : moverToDraw) {
				if (im.isDead())
					moverDead = im;
				else
					im.draw(buffer_canvas);
			}
			if (moverDead != null) {
				System.out.println("RIP " + moverDead);
				moverToDraw.remove(moverDead);
				moverDead = null;
			}
			moverToDrawLocker.release();
			// }
			if (bulletToDrawLocker.tryAcquire(100, TimeUnit.MILLISECONDS)) {
				// bulletToDrawLocker.acquireUninterruptibly()
				;
				for (IMover im : bulletToDraw) {
					if (im.isDead())
						moverDead = im;
					else
						im.draw(buffer_canvas);
				}
				if (moverDead != null) {
					bulletToDraw.remove(moverDead);
					moverDead = null;
				}
				bulletToDrawLocker.release();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		gui_string = teamRed.toString();
		drawHUD();
		showbuffer();
		// if (comportement == BattleFieldBehavior.MOVER)
		// aStar.computPath(perso.getNode(), pointDestination);
	}

	protected void PathDrawing(IMover perso) {
		utils.LIFO_Pool.Iterator<Node> itPath = aStar.getPath(perso);
		Node n, np;
		boolean directionDone = comportement == BattleFieldBehavior.MOVER;
		buffer_canvas.setColor(perso.getColor().darker().darker());
		np = itPath.next();
		while (itPath.hasNext()) {
			n = itPath.next();
			// drawDot(buffer_canvas, n, 6);
			if (np != null) {
				buffer_canvas.drawLine((int) np.x, (int) np.y, (int) n.x, (int) n.y);
				if (!directionDone) {
					directionDone = true;
					// System.out.println(n + " " + perso.getCoord());
					// perso.setDestination(n);
					// directionDone = !(perso.getCoord().equals(n));
				}
			}
			np = n;
		}
	}

	public void drawDot(Graphics g, Vector2d p, int size) {
		if (p == null)
			return;
		int x = (int) p.x;
		int y = (int) p.y;
		buffer_canvas.drawLine(x - size, y, x + size, y);
		buffer_canvas.drawLine(x, y - size, x, y + size);
	}

	public void drawRoundDot(Graphics g, Vector2d p, int size) {
		drawDot(g, p, size);
		g.drawOval((int) p.x - size, (int) p.y - size, size << 1, size << 1);
	}

	/*
	 * This is the main loop of the game. Sleeping, then updating positions then
	 * redrawing If you want a constant framerate, you should measure how much
	 * you'll have to sleep depending on the time eated by updates functions.
	 * 
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		do {
			repaint();
			try {
				Thread.sleep(60);
			} catch (InterruptedException _ex) {
			}
		} while (true);
	}

	// Use very simple double buffering technique...
	/**
	 * This is a very simple double buffering technique. Drawing are done
	 * offscreen, in the buffer_canvasimage canvas. Ones all drawings are done,
	 * we copy the whole canvas to the actual viewed canvas, viewer_canvas. Thus
	 * the player will only see a very fast update of its window. No flickering.
	 * 
	 */
	private void showbuffer() {
		viewer_canvas.drawImage(buffer_canvasimage, 0, 0, this);
	}

	public void start() {
		if (update == null) {
			update = new Thread(this);
			update.setPriority(Thread.MAX_PRIORITY);
			update.start();
		}
	}

	public void stop() {
		update = null;
		aStar.askToStop();
		moverManager.askToStop();
	}

	// Simply repaint the battle field... Called every frame...
	public void update(Graphics g) {
		paint(g);
	}

	public void iterateOnMoverToDraw(CodeExecutor<IMover> codeEx) {
		if (started != null)
			return;
		try {
			if (moverToDrawLocker.tryAcquire(100, TimeUnit.MILLISECONDS)) {
				for (IMover im : moverToDraw) {
					codeEx.execute(im);
					if (!codeEx.keepIterat()) {
						break;
					}
				}
				codeEx.endingExecution();
				moverToDrawLocker.release();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		} catch (ConcurrentModificationException e) {
			e.printStackTrace();
			System.err.println("Problem on iterating movers");
		}
	}

	public void addMover(IMover mover) {
		try {
			if (moverToDrawLocker.tryAcquire(100, TimeUnit.MILLISECONDS)) {
				moverManager.addMovers(mover);
				moverToDraw.add(mover);
				moverToDrawLocker.release();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public void addMover(IBullet mover) {
		if (started != null)
			return;
		try {
			if (bulletToDrawLocker.tryAcquire(100, TimeUnit.MILLISECONDS)) {
				moverManager.addMovers(mover);
				bulletToDraw.add(mover);
				bulletToDrawLocker.release();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
