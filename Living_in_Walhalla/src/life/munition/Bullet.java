package life.munition;

import java.awt.Color;
import java.awt.Graphics;

import life.IMover;
import life.ITeam;
import utils.CodeExecutor;
import utils.Vector2d;
import aStar2D.Node;
import applets.BattleField;

public class Bullet implements IBullet {

	private Vector2d vectSpeed;
	private Vector2d coord;
	private Vector2d tmp;
	private boolean iAmDead = false;
	private int damage = 10;
	private Bullet me = this;

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
		battleField.iterateOnMoverToDraw(new CodeExecutor<IMover>() {

			@Override
			public void execute(IMover mover) {
				if (mover != me && mover.getCoord().distance(coord) < (mover.getRadius() + me.getRadius())) {
					iAmDead = true;
					mover.hit(damage);
				}
			}

			@Override
			public boolean keepIterat() {
				return true;
			}
		});
		return true;
	}

	@Override
	public float getRadius() {
		return 1F;
	}

	@Override
	public Vector2d getCoord() {
		return coord;
	}

	@Override
	public void hit(int damage) {
		iAmDead = true;
	}

	@Override
	public Color getColor() {
		return Color.black;
	}

	@Override
	public Node getNode() {
		return null;
	}
}
