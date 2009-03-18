package aStar;


public class MyDot implements Dot {

	private String name;
	private float x;
	private float y;

	public MyDot(String name, float x, float y) {
		super();
		this.name = name;
		this.x = x;
		this.y = y;
	}

	@Override
	public float getX() {
		return x;
	}

	@Override
	public float getY() {
		return y;
	}

	@Override
	public float getZ() {
		return 0;
	}

	@Override
	public boolean is3d() {
		return false;
	}

	public String toString() {
		return name + ":(" + x + "," + y + ")";
	}

}
