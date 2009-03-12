package life.bots;

import java.awt.Graphics;

import life.ICharacter;
import life.IMover;
import life.ITeam;
import utils.CodeExecutor;
import aStar2D.Node;

public class Fantassin extends ICharacter {

	public Fantassin(Node startupPosition, ITeam team, String name) {
		super(startupPosition, team, name);
		// TODO Auto-generated constructor stub
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
		// TODO Auto-generated method stub

	}

	@Override
	public float getRadius() {
		// TODO Auto-generated method stub
		return 3;
	}

	@Override
	public void hit(int damage) {
		iAmDead |= ((life -= damage) < 0);
	}

	@Override
	protected void canShoot() {
		battleField.iterateOnMoverToDraw(new CodeExecutor<IMover>() {

			private boolean keepIterat = true;

			@Override
			public void execute(IMover param) {
//				System.out.println(name + " canShoot " + param);
				if (param.getTeam().isOpposedTo(me.getTeam())) {
					fire(param.getCoord());
					keepIterat = false;
				}
			}

			@Override
			public boolean keepIterat() {
				return keepIterat;
			}

		});
	}
}
