package life.bots;

import java.awt.Graphics;

import life.ICharacter;
import life.IMover;
import life.ITeam;
import life.munition.Bullet;
import utils.CodeExecutor;
import aStar2D.Node;

public class Fantassin extends ICharacter {
	public static int MY_LIFE = 100;

	public Fantassin(Node startupPosition, ITeam team, String name) {
		super(startupPosition, team, name);
		life = getInitialLife();
	}

	@Override
	public void drawCharacter(Graphics g) {
		int x = (int) coord.x;
		int y = (int) coord.y;
		g.setColor(team.getColor());
		g.drawLine((int) (x - getRadius()), (int) (y - getRadius()), (int) (x + getRadius()), (int) (y + getRadius()));
		g.drawLine((int) (x + 1 - getRadius()), (int) (y - getRadius()), (int) (x - 1 + getRadius()),
				(int) (y + getRadius()));
		g.drawLine((int) (x - getRadius()), (int) (y + getRadius()), (int) (x + getRadius()), (int) (y - getRadius()));
		g.drawLine((int) (x + 1 - getRadius()), (int) (y + getRadius()), (int) (x - 1 + getRadius()),
				(int) (y - getRadius()));
	}

	@Override
	public float getSpeed() {
		return 1;
	}

	@Override
	protected void journeyDone() {
		// TODO fantassin journeyDone

	}

	@Override
	public float getRadius() {
		return 3;
	}

	@Override
	protected void canShoot() {
		battleField.iterateOnMoverToDraw(new CodeExecutor<IMover>() {

			private boolean keepIterat = true;

			@Override
			public void execute(IMover param) {
				// System.out.println(name + " canShoot " + param);
				if (param.getTeam() != null && param.getTeam().isOpposedTo(me.getTeam())
						&& battleField.surface.canSee(param.getCoord(), me.getCoord())
						&& team.isNotFriendlyFire(me, param.getCoord())) {
					fire(new Bullet(me.getCoord(), param.getCoord()));
					keepIterat = false;
				}
			}

			@Override
			public boolean keepIterat() {
				return keepIterat;
			}

			@Override
			public void endingExecution() {
			}

		});
	}

	@Override
	public int getInitialLife() {
		return MY_LIFE;
	}
}
