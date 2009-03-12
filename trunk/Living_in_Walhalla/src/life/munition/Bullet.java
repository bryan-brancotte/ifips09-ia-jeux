package life.munition;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Iterator;

import life.IMover;
import life.ITeam;
import utils.Vector2d;
import applets.BattleField;

public class Bullet implements IBullet {

	private Vector2d vectSpeed;
	private Vector2d coord;
	private Vector2d tmp;
	private boolean iAmDead = false;
	private int damage = 10;

	protected static BattleField battleField;

	public static void init(BattleField battleField) {
		Bullet.battleField = battleField;
	}

	public Bullet(int speed, Vector2d startupPosition, Vector2d target) {
		super();
		this.vectSpeed = target.subtract(startupPosition);
		vectSpeed.setScale(speed / startupPosition.distance(target), vectSpeed);
		coord = new Vector2d(startupPosition.x, startupPosition.y);
		tmp = new Vector2d();
	}

	@Override
	public void draw(Graphics g) {
		g.setColor(Color.red);
		g.fillRect((int) (coord.x - getRadius()), (int) (coord.y - getRadius()), (int) getRadius() << 1,
				(int) getRadius() << 1);
	}

	@Override
	public ITeam getTeam() {
		return null;
	}

	@Override
	public boolean isDead() {
		return iAmDead;
	}

	@Override
	public boolean moveInItsDirection() {
		// System.out.println(coord + " += " + vectSpeed);
		tmp.setSum(coord, vectSpeed);
		iAmDead |= !battleField.surface.canSee(coord, tmp);
		coord.set(tmp);
		Iterator<IMover> itM = battleField.getMoverToDraw();
		IMover mover;
		while (itM.hasNext()) {
			mover = itM.next();
			if (mover != this && mover.getCoord().distance(coord) < (mover.getRadius() + this.getRadius())) {
				 iAmDead = true;
				mover.hit(damage);
			}
		}
		return true;
	}

	@Override
	public float getRadius() {
		return 2F;
	}

	@Override
	public Vector2d getCoord() {
		return coord;
	}

	@Override
	public void hit(int damage) {
		iAmDead = true;
	}
}
