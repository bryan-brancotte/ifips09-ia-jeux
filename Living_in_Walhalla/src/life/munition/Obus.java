package life.munition;

import java.awt.Color;
import java.awt.Graphics;

import life.IMover;
import life.ITeam;
import utils.CodeExecutor;
import utils.Vector2d;
import aStar2D.Node;
import applets.BattleField;

public class Obus implements IBullet {

	private Vector2d vectSpeed;
	private Vector2d coord;
	private Vector2d tmp;
	private boolean iAmDead = false;
	private int damage = 50;
	private Obus me = this;

	protected static BattleField battleField;

	public static void init(BattleField battleField) {
		Obus.battleField = battleField;
	}

	public Obus(Vector2d startupPosition, Vector2d target, float avancementInitial) {
		super();
		this.vectSpeed = target.subtract(startupPosition);
		vectSpeed.setScale(1.2F / startupPosition.distance(target), vectSpeed);
		coord = new Vector2d(startupPosition.x, startupPosition.y);
		if (avancementInitial > 0)
			for (int i = (int) (avancementInitial + getRadius()); i > 0; i--)
				coord.setSum(coord, vectSpeed);
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

			@Override
			public void endingExecution() {
				// TODO Auto-generated method stub
				
			}
		});
		return true;
	}

	@Override
	public float getRadius() {
		return 3F;
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

	@Override
	public long reloadingTime() {
		return (long) 3e9F;
	}
}
