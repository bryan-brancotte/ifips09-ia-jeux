package surface;

import utils.Vector2d;
import java.util.*;
import java.awt.*;

/**
 * A simple class to handle the surface itself. All objects are polylines.
 * 
 * @author L. Simon, Univ. Paris Sud, 2008.
 * 
 */
public class Surface {

	private int wxsize, wysize;

	// All objects on the surface are recorded in this vector of polylines.
	private Vector<PolylineObject> objects; // The objects on the surface

	public int getWxsize() {
		return wxsize;
	}

	public int getWysize() {
		return wysize;
	}

	/**
	 * Well, right now the objects are built "by hands". May by the first thing to do would be to put polylines objects
	 * in a map, and read the file and objects.
	 * 
	 * In this case, the size of the surface should be set inside the constructor?
	 * 
	 * @param wxsize
	 * @param wysize
	 * @param scale
	 */
	public Surface(int wxsize, int wysize, float scale) {
		this.wxsize = wxsize;
		this.wysize = wysize;
		objects = new Vector<PolylineObject>();

		PolylineObject ob;

		ob = new PolylineObject(new Vector2d(100F, 200F), this);
		ob.addNode(new Vector2d(100F, 250F));
		ob.addNode(new Vector2d(200F, 250F));
		ob.addNode(new Vector2d(200F, 200F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(300F, 50F));
		ob.addNode(new Vector2d(300F, 100F));
		ob.addNode(new Vector2d(400F, 100F));
		ob.addNode(new Vector2d(400F, 50F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(new Vector2d(300F, 500F), this);
		ob.addNode(new Vector2d(300F, 800F));
		ob.addNode(new Vector2d(320F, 800F));
		ob.addNode(new Vector2d(320F, 500F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(238F, 128F));
		ob.addNode(new Vector2d(272F, 183F));
		ob.addNode(new Vector2d(374F, 136F));
		ob.addNode(new Vector2d(195F, 323F));
		ob.addNode(new Vector2d(224F, 268F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(648F, 79F));
		ob.addNode(new Vector2d(620F, 444F));
		ob.addNode(new Vector2d(646F, 345F));
		ob.addNode(new Vector2d(711F, 28F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(209F, 301F));
		ob.addNode(new Vector2d(336F, 400F));
		ob.addNode(new Vector2d(383F, 374F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(383F, 374F));
		ob.addNode(new Vector2d(591F, 200F));
		ob.addNode(new Vector2d(553F, 179F));
		ob.addNode(new Vector2d(314F, 383F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(311F, 284F));
		ob.addNode(new Vector2d(343F, 299F));
		ob.addNode(new Vector2d(655F, -15F));
		ob.addNode(new Vector2d(624F, -55F));
		ob.fixObject();
		objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
		//
		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
	}

	/**
	 * Draws all objects on the surface.
	 * 
	 * @param g
	 */
	public void draw(Graphics g) {
		g.setColor(Color.BLACK);
		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).draw(g);
		}
	}

	/**
	 * One of the main methods. Checks if the segment (tmpA, tmpB) intersects any of the lines of any polyline. If not,
	 * then the point tmpA 'can see' the point tmpB.
	 * 
	 * @param tmpA
	 * @param tmpB
	 * @return true if tmpA can see tmpB
	 */
	public boolean canSee(Vector2d tmpA, Vector2d tmpB) {
		if (tmpA == null || tmpB == null)
			return false;
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).intersectsWith(tmpA, tmpB))
				return false;
		}
		return true;

	}

	/**
	 * //TODO
	 * 
	 * @param tmp
	 * @return true if tmpA can see tmpB
	 */
	public boolean isInAConvexeObject(Vector2d tmp) {
		for (PolylineObject obj : objects)
			if (!obj.intersectsWith(obj.getCoordCenterPoint(), tmp))
				return true;
		return false;

	}

	/**
	 * 
	 * @param tmp
	 * @return true if tmpA can see tmpB
	 */
	public boolean isInAnObject(Vector2d tmp) {
		return isInAConvexeObject(tmp);

	}

	public Iterator<PolylineObject> getObject() {
		return objects.iterator();
	}

}
