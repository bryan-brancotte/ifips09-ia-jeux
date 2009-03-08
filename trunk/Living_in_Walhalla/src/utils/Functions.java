package utils;

import aStar.Dot;

public abstract class Functions {

	public static final float getDistanceFrom(Dot dot1, Dot dot2) {
		float ret;
		float val = dot1.getX() - dot2.getX();
		ret = val * val;
		val = dot1.getY() - dot2.getY();
		ret += val * val;
		if (dot2.is3d() && dot1.is3d()) {
			val = dot1.getZ() - dot2.getZ();
			ret += val * val;
		}
		// utilisation de la racine carré de John Carmack révisé par Chris Lomont
		// Pour John Carmack : 0x5f3759df
		// Pour Chris Lomont : 0x5f375a86
		// les temps de calcul se retourve divisé par 2
		int i;
		val = ret * 0.5F;
		i = Float.floatToRawIntBits(ret);
		i = 0x5f375a86 - (i >> 1);
		ret = Float.intBitsToFloat(i);
		ret = ret * (1.5F - (val * ret * ret));

		return (1.0F / ret);
	}

	public static float squareRootFloat(float n) {
		int i;
		float x2, y;
		x2 = n * 0.5F;
		y = n;
		i = Float.floatToRawIntBits(y);
		i = 0x5f375a86 - (i >> 1);
		y = Float.intBitsToFloat(i);
		y = y * (1.5F - (x2 * y * y));

		return (long) (1.0F / y);
	}
}
