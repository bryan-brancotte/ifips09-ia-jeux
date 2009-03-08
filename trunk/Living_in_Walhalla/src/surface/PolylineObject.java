package surface;

import java.awt.Graphics;
import java.awt.Polygon;
import java.util.Vector;

import utils.Vector2d;

/**
 * Allow to handle any polyline object on the map... No Splines yet... And 2D :(
 * 
 * @author L. Simon, Univ. Paris Sud, 2008
 */
public class PolylineObject {

	protected int nbPoints;
	protected Vector2d coordFirstPoint;
	protected Vector2d coordCenterPoint;
	private boolean visible;
	static private Vector2d tmpvect = new Vector2d();

	// Some cached values
	protected Vector<Vector2d> globalCoordPoints;
	private Polygon cachedPolygon;

	/**
	 * @param AnchorPoint
	 *            The global coordonates of the anchor point in the surface
	 * @param scale
	 *            a scaling factor applied to all local coordinates
	 */
	protected PolylineObject(Vector2d AnchorPoint, Surface onsurface) {
		nbPoints = 1;
		visible = true;
		globalCoordPoints = new Vector<Vector2d>();
		globalCoordPoints.add(new Vector2d(AnchorPoint.x, AnchorPoint.y));
	}

	/**
	 * @param scale
	 *            a scaling factor applied to all local coordinates
	 */
	protected PolylineObject(Surface onsurface) {
		nbPoints = 0;
		visible = true;
		globalCoordPoints = new Vector<Vector2d>();
	}

	protected void addNode(Vector2d localCoord) {
		globalCoordPoints.add(localCoord);
		nbPoints++;
	}

	protected void fixObject() {
		cachedPolygon = new Polygon();
		for (int i = 0; i < nbPoints; i++) {
			cachedPolygon.addPoint((int) (globalCoordPoints.get(i).x), (int) (globalCoordPoints.get(i).y));
		}
	}

	/**
	 * intersection entre [AB] et [CD] à mettre dans result
	 * 
	 * @param A
	 * @param B
	 * @param C
	 * @param D
	 * @param result
	 * @return
	 */
	public static boolean twoSegmentsIntersection(Vector2d A, Vector2d B, Vector2d C, Vector2d D, Vector2d result) {
		float Sx;
		float Sy;

		if (A.x == B.x) {
			if (C.x == D.x)
				return false;
			else {
				float pCD = (C.y - D.y) / (C.x - D.x);
				Sx = A.x;
				Sy = pCD * (A.x - C.x) + C.y;
			}
		} else {
			if (C.x == D.x) {
				float pAB = (A.y - B.y) / (A.x - B.x);
				Sx = C.x;
				Sy = pAB * (C.x - A.x) + A.y;
			} else {
				float pCD = (C.y - D.y) / (C.x - D.x);
				float pAB = (A.y - B.y) / (A.x - B.x);
				float oCD = C.y - pCD * C.x;
				float oAB = A.y - pAB * A.x;
				Sx = (oAB - oCD) / (pCD - pAB);
				if (A.y == B.y)
					Sy = A.y;
				else if (C.y == D.y)
					Sy = C.y;
				else
					Sy = pCD * Sx + oCD;
			}
		}
		if ((Sx < A.x && Sx < B.x) || (Sx > A.x && Sx > B.x) || (Sx < C.x && Sx < D.x) | (Sx > C.x && Sx > D.x)
				|| (Sy < A.y && Sy < B.y) || (Sy > A.y && Sy > B.y) || (Sy < C.y && Sy < D.y) || (Sy > C.y && Sy > D.y))
			return false;
		return true;
	}

	/**
	 * Return True if the segment intersects the polyline object. Note: We consider Vector3 as Vector2 vectors only here
	 * (z=0);
	 * 
	 * @param pointA
	 * @param pointB
	 * @return the point of intersection (via result) and true, if...
	 */
	public boolean closestPointOfIntersectionWith(Vector2d pointA, Vector2d pointB, Vector2d result) {
		float bestLength = -1F;
		boolean interesects = false;
		for (int i = 0; i < nbPoints; i++) {
			// TODO optimisation i + 1 >= nbPoints ? 0 : i + 1 par (i+1)%nbPoints
			if (twoSegmentsIntersection(pointA, pointB, globalCoordPoints.get(i), globalCoordPoints.get((i + 1)
					% nbPoints), tmpvect)) {
				interesects = true;
				float newLength = pointA.distance(tmpvect);
				if ((bestLength < 0F) || (newLength < bestLength)) {
					bestLength = newLength;
					result.set(tmpvect);
				}
			}
		}
		return interesects;
	}

	public boolean intersectsWith(Vector2d pointA, Vector2d pointB) {
		// return closestPointOfIntersectionWith(pointA, pointB, tmpvect);
		// Vector2d oldP = globalCoordPoints.lastElement();
		// for (Vector2d v : globalCoordPoints)
		// if (twoSegmentsIntersection(pointA, pointB, oldP, v, tmpvect))
		for (int i = 0; i < nbPoints; i++)
			if (twoSegmentsIntersection(pointA, pointB, globalCoordPoints.get(i), globalCoordPoints.get((i + 1)
					% nbPoints), tmpvect))
				return true;
		return false;
	}

	@Deprecated
	public Vector2d getCoordFirstPoint() {
		return coordFirstPoint;
	}

	public Vector2d getCoordCenterPoint() {
		if (coordCenterPoint == null) {
			float x = 0;
			float y = 0;
			for (Vector2d v : globalCoordPoints) {
				x += v.x;
				y += v.y;
			}
			x /= globalCoordPoints.size();
			y /= globalCoordPoints.size();
			coordCenterPoint = new Vector2d(x, y);
		}
		return coordCenterPoint;
	}

	protected Vector<Vector2d> getCoords() {
		return globalCoordPoints;
	}

	public boolean isVisible() {
		return visible;
	}

	protected void setVisible(boolean visible) {
		this.visible = visible;
	}

	public void draw(Graphics g) {
		g.fillPolygon(cachedPolygon);
		// g.drawPolygon(cachedPolygon.xpoints, cachedPolygon.ypoints, cachedPolygon.npoints);
	}

}
