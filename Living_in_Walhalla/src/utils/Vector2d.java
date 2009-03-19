package utils;

import java.util.Random;

/**
 * Class to handle some 2D computations, taken from differents sources.
 * 
 * @author L. Simon, Univ. Paris Sud, 2008
 * 
 */
public class Vector2d {

	@SuppressWarnings("unused")
	private static Vector2d distTemp = new Vector2d();

	public static Random generator = new Random();

	public static final Vector2d zero = new Vector2d();

	public static Vector2d unitRandom() {
		Vector2d v = new Vector2d(0.0F, 0.0F);
		do {
			v.x = generator.nextFloat() * 2.0F - 1.0F;
			v.y = generator.nextFloat() * 2.0F - 1.0F;
		} while (v.magnitudeSquared() > 1.0F);
		return v;
	}

	public float x;

	public float y;

	public Vector2d() {
		setZero();
	}

	public Vector2d(float x, float y) {
		set(x, y);
	}

	/**
	 * retourne un nouveau vecteur 2d qui est la somme de this et de celui passé
	 * en paramètre
	 * 
	 * @param other
	 * @return
	 */
	public Vector2d add(Vector2d other) {
		return new Vector2d(x + other.x, y + other.y);
	}

	/**
	 * distance du point courant au point passé en paramètre
	 * 
	 * @param that
	 * @return
	 */
	public float distance(Vector2d that) {
		// return (float) Math.sqrt((x - that.x) * (x - that.x) + (y * that.y) *
		// (y * that.y));/*
		float ret;
		float val = this.x - that.x;
		ret = val * val;
		val = this.y - that.y;
		ret += val * val;
		// utilisation de la racine carré de John Carmack révisé par Chris
		// Lomont
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
		/**/
	}

	/**
	 * distance du point courant au point passé en paramètre
	 * 
	 * @param that
	 * @return
	 */
	public float distanceCarre(Vector2d that) {
//		return (x - that.x) * (x - that.x) + (y * that.y) * (y * that.y);
		return distance(that);
	}

	public float norme() {
		// return (float) Math.sqrt((x - that.x) * (x - that.x) + (y * that.y) *
		// (y * that.y));/*
		float ret;
		float val = this.x;
		ret = val * val;
		val = this.y;
		ret += val * val;
		// utilisation de la racine carré de John Carmack révisé par Chris
		// Lomont
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

	public float distance(float x, float y) {
		// return (float) Math.sqrt((x - this.x) * (x - this.x) + (y * this.y) *
		// (y * this.y));/*
		float ret;
		float val = this.x - x;
		ret = val * val;
		val = this.y - y;
		ret += val * val;
		// utilisation de la racine carré de John Carmack révisé par Chris
		// Lomont
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
		/**/
	}

	public float dot(Vector2d that) {
		return x * that.x + y * that.y;
	}

	public boolean equals(Vector2d that) {
		return that != null && x == that.x && y == that.y;
	}

	public boolean equalsZero() {
		return x == 0.0F && y == 0.0F;
	}

	public float magnitude() {
		return (float) Math.sqrt(magnitudeSquared());
	}

	public float magnitudeSquared() {
		return x * x + y * y;
	}

	public Vector2d scale(float factor) {
		return new Vector2d(x * factor, y * factor);
	}

	public void set(float x, float y) {
		this.x = x;
		this.y = y;
	}

	public void set(Vector2d that) {
		x = that.x;
		y = that.y;
	}

	public void setApproximateNormalize() {
		float m = magnitude();
		if (m != 0.0F)
			setScale(1.0F / m, this);
	}

	public void setApproximateTruncate(float threshold) {
		float length = magnitude();
		if (length > threshold)
			setScale(threshold / length, this);
	}

	public void setCross(Vector2d a, Vector2d b) {
		set(a.y * b.x - a.x * b.y, a.y * b.x - a.x * b.y);
	}

	public void setDiff(Vector2d a, Vector2d b) {
		x = a.x - b.x;
		y = a.y - b.y;
	}

	public void setInterp(float blend, Vector2d v0, Vector2d v1) {
		x = v0.x + blend * (v1.x - v0.x);
		y = v0.y + blend * (v1.y - v0.y);
	}

	public void setNormalize() {
		float m = magnitude();
		if (m != 0.0F)
			setScale(1.0F / m, this);
	}

	public void setScale(float a, Vector2d b) {
		x = a * b.x;
		y = a * b.y;
	}

	public void setSum(Vector2d a, Vector2d b) {
		x = a.x + b.x;
		y = a.y + b.y;
	}

	public void setUnitRandom() {
		do {
			x = generator.nextFloat() * 2.0F - 1.0F;
			y = generator.nextFloat() * 2.0F - 1.0F;
		} while (magnitudeSquared() > 1.0F);
	}

	public void setZero() {
		set(0.0F, 0.0F);
	}

	public Vector2d subtract(Vector2d other) {
		return new Vector2d(x - other.x, y - other.y);
	}

	public String toString() {
		return "(" + x + "," + y + ")";
	}

}
