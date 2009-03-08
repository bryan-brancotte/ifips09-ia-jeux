package applets;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Event;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.LinkedList;

import surface.Surface;
import utils.Vector2d;
import waypoint.WaypointInstaller;
import aStar2D.AStarMultiThread;
import aStar2D.Node;
import bots.Cow;
import bots.IMover;
import bots.MoverManager;
import bots.Personnage;

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

	static final public int PREF_VIEWER_XSIZE = 800; // size in pixels (in x,
	// the y is
	// automatically
	// deduced)
	private static final long serialVersionUID = 1L;
	protected BattleFieldBehavior comportement = BattleFieldBehavior.MOVER;
	protected boolean DRAW_PATH_AND_WAYPOINT = true;

	protected MoverManager moverManager;

	public static void main(String args[]) {
		Frame f = new Frame();
		BattleField app = new BattleField();
		app.init();
		app.start();
		app.stop();
		f.add("Center", app);
	}

	Graphics buffer_canvas; // Where to draw (off-screen buffer)
	// Canvas for double buffering
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
	private LinkedList<IMover> moverToDraw;
	public Surface surface; // The surface that contains the objects...
	Node[] waypoints;
	AStarMultiThread aStar;

	/**
	 * Thread that sleeps and update the screen.
	 */
	private Thread update;

	Graphics viewer_canvas; // What the user actually see (on-screen buffer)

	// Viewer variables
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
		buffer_canvas.setColor(Color.red);
		buffer_canvas.drawRect(20, viewer_ysize - 23, viewer_xsize - 41, 20);
		buffer_canvas.drawChars(gui_string.toCharArray(), 0, Math.min(80, gui_string.length()), 22, viewer_ysize - 7);
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
		// ((Graphics2D)
		// buffer_canvas).setRenderingHint(RenderingHints.KEY_ANTIALIASING,
		// RenderingHints.VALUE_ANTIALIAS_ON);
		viewer_canvas = this.getGraphics();

		addMouseListener(this);
		addMouseMotionListener(this);

		initSurface();
		initWaypoint();
		initaStar();
		initBots();
		initBelettes();
	}

	/**
	 * Called ones to init all your belettes structures.
	 */
	public void initBelettes() {
		// TODO
	}

	/**
	 * Called ones to init all your bots.
	 */
	public void initBots() {
		moverToDraw = new LinkedList<IMover>();
		IMover im;

		perso = new Personnage(this, aStar, Color.red, "Florence");
		perso2 = new Personnage(this, aStar, Color.blue, "Bryan");
		perso.setDestination(waypoints[100]);
		perso2.setDestination(waypoints[300]);
		moverManager = new MoverManager(10);
		moverManager.start();
		moverManager.addMovers(perso);
		moverManager.addMovers(perso2);
		for (int i = 0; i < 10; i++) {
			moverManager.addMovers(im = new Cow(this, aStar, waypoints[Cow.rand.nextInt(waypoints.length)], waypoints));
			moverToDraw.add(im);
		}
		perso.updatePosition();
		perso2.updatePosition();
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
		// waypoints = WaypointInstaller.placeGridAndRandomWaypoint(surface,
		// 5);/*
		// waypoints = WaypointInstaller.placeGridWaypoint(surface, 5);/*
		// waypoints = WaypointInstaller.placeRandomWaypoint(surface, 500);/*
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

	/*
	 * TODO: use this method your own way.
	 * 
	 * @see
	 * java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
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

	/*
	 * Here we memorize the mouse position to draw lines where points can see
	 * eachother. TODO: you must handle mouse events in your game.
	 * 
	 * @see
	 * java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
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
			perso.setDestination(pointDestination);
			perso2.setDestination(pointDestination);
		}
		System.out.println("ob.addNode(new Vector2d(" + e.getX() * viewer_scale + "F, " + e.getY() * viewer_scale
				+ "F));");
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

		// 3. TODO: Draw the bots in their position/direction

		// 4. TODO: Draw the bullets / Special Effects.

		// 5. Draw the Waypoint
		// if (DRAW_PATH_AND_WAYPOINT && waypoints != null /*
		// * && waypoints.length <
		// * 100/
		// */) {
		// Iterator<Node.Link> it;
		// Node.Link wf;
		// buffer_canvas.setColor(Color.yellow);
		// for (Node w : waypoints) {
		// drawDot(buffer_canvas, w, 4);
		// it = w.getNeighbor();
		// while ((wf = it.next()) != null) {
		// buffer_canvas.drawLine((int) w.x, (int) w.y, (int) wf.getNode().x,
		// (int) wf.getNode().y);
		// }
		// }
		// }

		// 6 Draw the begeining and the end
		if (DRAW_PATH_AND_WAYPOINT) {
			// 6. Draw the path
			PathDrawing(perso);
			PathDrawing(perso2);
			for (IMover im : moverToDraw)
				PathDrawing(im);
			// else
			// while (!directionDone && itPath.hasNext()) {
			// n = itPath.next();
			// if (n.getPreviousNode() != null) {
			// directionDone = true;
			// perso.setDestination(n);
			// // directionDone = !(perso.getCoord().equals(n));
			// }
			// }
		}
		gui_string = "[Coût du chemin : " + ((int) (aStar.getCostPath(perso) * 1000) / 1000F) + "]";

		// System.out.println("pointDestination:" + pointDestination + " at " +
		// cost + " from your clic");
		// buffer_canvas.setColor(Color.blue);
		// drawDot(buffer_canvas, perso.getNode(), 5);
		perso.draw(buffer_canvas);
		// drawRoundDot(buffer_canvas,perso.getCoord(),10);
		perso2.draw(buffer_canvas);
		for (IMover im : moverToDraw)
			im.draw(buffer_canvas);

		drawHUD();
		showbuffer();
		// if (comportement == BattleFieldBehavior.MOVER)
		// aStar.computPath(perso.getNode(), pointDestination);
	}

	protected void PathDrawing(IMover perso) {
		utils.LIFO_Pool.Iterator<Node> itPath = aStar.getPath(perso);
		Node n, np;
		boolean directionDone = comportement == BattleFieldBehavior.MOVER;
		buffer_canvas.setColor(Color.blue);
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
			updateBelettes();
			updateBots();
			repaint();
			try {
				Thread.sleep(50);
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

	/**
	 * Must update bullets positions and handles damages to bots...
	 */
	public void updateBelettes() {
		// TODO: nothing here yet
	}

	/**
	 * Must update bots position / decisions / health This is where your AI will
	 * be called.
	 * 
	 */
	public void updateBots() {
		// TODO: You have to update all your bots here.
	}

}
