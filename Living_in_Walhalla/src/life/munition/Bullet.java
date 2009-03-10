package life.munition;

import java.awt.Color;
import java.awt.Graphics;

import life.IMover;
import life.ITeam;
import utils.Vector2d;
import applets.BattleField;

public class Bullet implements IMover {

	private Vector2d vectSpeed;
	private Vector2d coord;
	private Vector2d tmp;
	private boolean iAmDead = false;

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
		g.fillRect((int) coord.x - 2, (int) (coord.y - 2), 4, 4);
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
		return true;
	}
}
