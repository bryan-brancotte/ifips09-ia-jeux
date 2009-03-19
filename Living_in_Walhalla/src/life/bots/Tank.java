package life.bots;

import java.awt.Graphics;

import life.ICharacter;
import life.IMover;
import life.ITeam;
import life.munition.Bullet;
import life.munition.Obus;
import utils.CodeExecutor;
import aStar2D.Node;

public class Tank extends ICharacter {
	public static int MY_LIFE = 400;

	public Tank(Node startupPosition, ITeam team, String name) {
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
		return 0.6F;
	}

	@Override
	protected void journeyDone() {
	}

	@Override
	public float getRadius() {
		return 8;
	}

	@Override
	protected void canShoot() {
		battleField.iterateOnMoverToDraw(new CodeExecutor<IMover>() {

			private boolean keepIterat = true;
			private IMover target = null;
			private float dstCarre;
			private float tmp;

			@Override
			public void execute(IMover param) {
				// System.out.println(name + " canShoot " + param);
				if (param.getTeam() != null && param.getTeam().isOpposedTo(me.getTeam())
						&& battleField.surface.canSee(param.getCoord(), me.getCoord())
						&& team.isNotFriendlyFire(me, param.getCoord())) {
					tmp = me.getCoord().distanceCarre(param.getCoord());
					if (target == null || tmp < dstCarre) {
						dstCarre = tmp;
						target = param;
					}
				}
			}

			@Override
			public boolean keepIterat() {
				return keepIterat;
			}

			@Override
			public void endingExecution() {
				if (target != null) {
					System.out.println(dstCarre);
					if (dstCarre < 300)
						fire(new Obus(me.getCoord(), target.getCoord(), me.getRadius()));
					else
						fire(new Bullet(me.getCoord(), target.getCoord(), me.getRadius()));
				}
			}

		});
	}

	@Override
	public int getInitialLife() {
		return MY_LIFE;
	}
}
