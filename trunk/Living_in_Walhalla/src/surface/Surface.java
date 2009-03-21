package surface;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;
import java.util.Vector;

import utils.Vector2d;

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
	 * Well, right now the objects are built "by hands". May by the first thing
	 * to do would be to put polylines objects in a map, and read the file and
	 * objects.
	 * 
	 * In this case, the size of the surface should be set inside the
	 * constructor?
	 * 
	 * @param wxsize
	 * @param wysize
	 * @param scale
	 */
	public Surface(int wxsize, int wysize, float scale) {
		this.wxsize = wxsize;
		this.wysize = wysize;
		objects = new Vector<PolylineObject>();
		lab();
		// firstSurface();
		// rectangle();
		PolylineObject ob;

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(0F, 1F));
		ob.addNode(new Vector2d(800F, 1F));
		ob.addNode(new Vector2d(800F, 0F));
		ob.addNode(new Vector2d(0F, 0F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(0F, 599F));
		ob.addNode(new Vector2d(800F, 599F));
		ob.addNode(new Vector2d(800F, 600F));
		ob.addNode(new Vector2d(0F, 600F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(1F, 0F));
		ob.addNode(new Vector2d(1F, 600F));
		ob.addNode(new Vector2d(0F, 600F));
		ob.addNode(new Vector2d(0F, 0F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(799F, 0F));
		ob.addNode(new Vector2d(799F, 600F));
		ob.addNode(new Vector2d(800F, 600F));
		ob.addNode(new Vector2d(800F, 0F));
		ob.fixObject();
		objects.add(ob);
	}

	protected void firstSurface() {
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
		ob.addNode(new Vector2d(203F, 315F));
		ob.addNode(new Vector2d(224F, 268F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(648F, 79F));
		ob.addNode(new Vector2d(620F, 444F));
		ob.addNode(new Vector2d(630F, 452F));
		ob.addNode(new Vector2d(646F, 395F));
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

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(300F, 63F));
		ob.addNode(new Vector2d(65F, 69F));
		ob.addNode(new Vector2d(66F, 87F));
		ob.addNode(new Vector2d(309F, 78F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(40F, 68F));
		ob.addNode(new Vector2d(47F, 548F));
		ob.addNode(new Vector2d(63F, 550F));
		ob.addNode(new Vector2d(73F, 63F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(146F, 320F));
		ob.addNode(new Vector2d(132F, 605F));
		ob.addNode(new Vector2d(156F, 604F));
		ob.addNode(new Vector2d(158F, 307F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(135F, 293F));
		ob.addNode(new Vector2d(552F, 539F));
		ob.addNode(new Vector2d(549F, 546F));
		ob.addNode(new Vector2d(142F, 331F));

		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(505F, 338F));
		ob.addNode(new Vector2d(502F, 465F));
		ob.addNode(new Vector2d(596F, 412F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(623F, 562F));
		ob.addNode(new Vector2d(726F, 393F));
		ob.addNode(new Vector2d(744F, 418F));
		ob.addNode(new Vector2d(674F, 573F));
		ob.fixObject();
		objects.add(ob);

		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);

		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);

		// ob = new PolylineObject(this);
		// ob.fixObject();
		// objects.add(ob);
	}

	protected void rectangle() {
		PolylineObject ob;

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(92F, 50F));
		ob.addNode(new Vector2d(92F, 280F));
		ob.addNode(new Vector2d(108F, 280F));
		ob.addNode(new Vector2d(108F, 50F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 50F));
		ob.addNode(new Vector2d(708F, 280F));
		ob.addNode(new Vector2d(692F, 280F));
		ob.addNode(new Vector2d(692F, 50F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(92F, 550F));
		ob.addNode(new Vector2d(92F, 320F));
		ob.addNode(new Vector2d(108F, 320F));
		ob.addNode(new Vector2d(108F, 550F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 550F));
		ob.addNode(new Vector2d(708F, 320F));
		ob.addNode(new Vector2d(692F, 320F));
		ob.addNode(new Vector2d(692F, 550F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(92F, 50F));
		ob.addNode(new Vector2d(92F, 66F));
		ob.addNode(new Vector2d(380F, 66F));
		ob.addNode(new Vector2d(380F, 50F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 50F));
		ob.addNode(new Vector2d(708F, 66F));
		ob.addNode(new Vector2d(420F, 66F));
		ob.addNode(new Vector2d(420F, 50F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(92F, 550F));
		ob.addNode(new Vector2d(92F, 534F));
		ob.addNode(new Vector2d(380F, 534F));
		ob.addNode(new Vector2d(380F, 550F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 550F));
		ob.addNode(new Vector2d(708F, 534F));
		ob.addNode(new Vector2d(420F, 534F));
		ob.addNode(new Vector2d(420F, 550F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(220F, 110F));
		ob.addNode(new Vector2d(580F, 110F));
		ob.addNode(new Vector2d(580F, 126F));
		ob.addNode(new Vector2d(220F, 126F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(220F, 490F));
		ob.addNode(new Vector2d(580F, 490F));
		ob.addNode(new Vector2d(580F, 474F));
		ob.addNode(new Vector2d(220F, 474F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(160F, 200F));
		ob.addNode(new Vector2d(160F, 400F));
		ob.addNode(new Vector2d(176F, 400F));
		ob.addNode(new Vector2d(176F, 200F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(640F, 200F));
		ob.addNode(new Vector2d(640F, 400F));
		ob.addNode(new Vector2d(624F, 400F));
		ob.addNode(new Vector2d(624F, 200F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(300F, 300F));
		ob.addNode(new Vector2d(400F, 200F));
		ob.addNode(new Vector2d(500F, 300F));
		ob.addNode(new Vector2d(400F, 400F));
		ob.fixObject();
		objects.add(ob);
	}

	protected void lab() {
		PolylineObject ob;

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(115F, 311F));
		ob.addNode(new Vector2d(105F, 600F));
		ob.addNode(new Vector2d(139F, 600F));
		ob.addNode(new Vector2d(140F, 348F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(172F, 282F));
		ob.addNode(new Vector2d(211F, 238F));
		ob.addNode(new Vector2d(253F, 272F));
		ob.addNode(new Vector2d(217F, 328F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(285F, 225F));
		ob.addNode(new Vector2d(278F, 0F));
		ob.addNode(new Vector2d(324F, 0F));
		ob.addNode(new Vector2d(319F, 266F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(326F, 332F));
		ob.addNode(new Vector2d(336F, 580F));
		ob.addNode(new Vector2d(297F, 570F));
		ob.addNode(new Vector2d(284F, 348F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(376F, 050F));
		ob.addNode(new Vector2d(376F, 453F));
		ob.addNode(new Vector2d(401F, 453F));
		ob.addNode(new Vector2d(401F, 050F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(451F, 0F));
		ob.addNode(new Vector2d(458F, 281F));
		ob.addNode(new Vector2d(508F, 282F));
		ob.addNode(new Vector2d(501F, 0F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(511F, 346F));
		ob.addNode(new Vector2d(509F, 600F));
		ob.addNode(new Vector2d(459F, 600F));
		ob.addNode(new Vector2d(467F, 336F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(683F, 47F));
		ob.addNode(new Vector2d(673F, 156F));
		ob.addNode(new Vector2d(710F, 153F));
		ob.addNode(new Vector2d(713F, 47F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(758F, 179F));
		ob.addNode(new Vector2d(751F, 337F));
		ob.addNode(new Vector2d(773F, 337F));
		ob.addNode(new Vector2d(777F, 184F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(677F, 296F));
		ob.addNode(new Vector2d(662F, 513F));
		ob.addNode(new Vector2d(701F, 513F));
		ob.addNode(new Vector2d(701F, 312F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(755F, 494F));
		ob.addNode(new Vector2d(689F, 557F));
		ob.addNode(new Vector2d(712F, 565F));
		ob.addNode(new Vector2d(767F, 506F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(619F, 494F));
		ob.addNode(new Vector2d(617F, 577F));
		ob.addNode(new Vector2d(586F, 577F));
		ob.addNode(new Vector2d(596F, 489F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(599F, 411F));
		ob.addNode(new Vector2d(542F, 487F));
		ob.addNode(new Vector2d(557F, 495F));
		ob.addNode(new Vector2d(609F, 436F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 203F));
		ob.addNode(new Vector2d(662F, 243F));
		ob.addNode(new Vector2d(681F, 273F));
		ob.addNode(new Vector2d(731F, 213F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(718F, 198F));
		ob.addNode(new Vector2d(647F, 240F));
		ob.addNode(new Vector2d(666F, 260F));
		ob.addNode(new Vector2d(719F, 225F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(708F, 203F));
		ob.addNode(new Vector2d(662F, 243F));
		ob.addNode(new Vector2d(681F, 273F));
		ob.addNode(new Vector2d(731F, 213F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(171F, 448F));
		ob.addNode(new Vector2d(221F, 356F));
		ob.addNode(new Vector2d(248F, 367F));
		ob.addNode(new Vector2d(182F, 449F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(185F, 197F));
		ob.addNode(new Vector2d(244F, 111F));
		ob.addNode(new Vector2d(250F, 157F));
		ob.addNode(new Vector2d(190F, 226F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(118F, 38F));
		ob.addNode(new Vector2d(16F, 186F));
		ob.addNode(new Vector2d(40F, 220F));
		ob.addNode(new Vector2d(147F, 60F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(208F, 30F));
		ob.addNode(new Vector2d(185F, 133F));
		ob.addNode(new Vector2d(233F, 31F));
		;
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(43F, 336F));
		ob.addNode(new Vector2d(19F, 493F));
		ob.addNode(new Vector2d(68F, 415F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(151F, 245F));
		ob.addNode(new Vector2d(95F, 208F));
		ob.addNode(new Vector2d(132F, 279F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(62F, 249F));
		ob.addNode(new Vector2d(46F, 322F));
		ob.addNode(new Vector2d(94F, 301F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(461F, 306F));
		ob.addNode(new Vector2d(424F, 281F));
		ob.addNode(new Vector2d(424F, 346F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(260F, 530F));
		ob.addNode(new Vector2d(170F, 535F));
		ob.addNode(new Vector2d(216F, 562F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(578F, 124F));
		ob.addNode(new Vector2d(561F, 293F));
		ob.addNode(new Vector2d(605F, 197F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(611F, 240F));
		ob.addNode(new Vector2d(562F, 360F));
		ob.addNode(new Vector2d(616F, 352F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(551F, 107F));
		ob.addNode(new Vector2d(609F, 21F));
		ob.addNode(new Vector2d(610F, 81F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(646F, 57F));
		ob.addNode(new Vector2d(642F, 11F));
		ob.addNode(new Vector2d(719F, 14F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);
		ob.addNode(new Vector2d(546F, 139F));
		ob.addNode(new Vector2d(587F, 183F));
		ob.addNode(new Vector2d(537F, 252F));
		ob.fixObject();
		objects.add(ob);

		ob = new PolylineObject(this);

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
	 * One of the main methods. Checks if the segment (tmpA, tmpB) intersects
	 * any of the lines of any polyline. If not, then the point tmpA 'can see'
	 * the point tmpB.
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
