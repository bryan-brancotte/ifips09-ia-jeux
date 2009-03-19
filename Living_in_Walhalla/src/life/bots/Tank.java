package life.bots;

import java.awt.Color;
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
		int radius = (int) getRadius();
		g.setColor(team.getColor());
		g.fillRect(x - radius, y - (radius >> 1), radius << 1, radius >> 1);
		g.fillPolygon(new int[] { x - (radius >> 1) - (radius >> 2) - 2, x - (radius >> 1), x + (radius >> 1),
				x + (radius >> 1) + (radius >> 2) + 1 }, new int[] { y - (radius >> 1), y - radius, y - radius,
				y - (radius >> 1) }, 4);
		g.drawLine(x, y - (radius >> 1) - (radius >> 2), x + radius, y - (radius >> 1) - (radius >> 2));
		g.setColor(Color.black);
		g.drawLine(x - (radius >> 1) - (radius >> 2), y, x - (radius >> 2), y);
		g.drawLine(x + (radius >> 1) + (radius >> 2) - 1, y, x - 1 + (radius >> 2), y);
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
			private float dst;
			private float tmp;

			@Override
			public void execute(IMover param) {
				// System.out.println(name + " canShoot " + param);
				if (param.getTeam() != null && param.getTeam().isOpposedTo(me.getTeam())
						&& battleField.surface.canSee(param.getCoord(), me.getCoord())
						&& team.isNotFriendlyFire(me, param.getCoord())) {
					tmp = me.getCoord().distance(param.getCoord());
					if (target == null || tmp < dst) {
						dst = tmp;
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
					if (dst < 300)
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
