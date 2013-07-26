package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.converters.TypeConverter;

public class Vector2fConverter implements TypeConverter<Vector2f> {
	
	// Example of a converter you have to create for your own mutable class if you want it to be interpolated.
	
	@Override
	public float[] copyFromObject(Vector2f v, float[] x) {
		float[] xCopy = x;
		if (xCopy == null) {
			xCopy = new float[variables()];
		}
		xCopy[0] = v.x;
		xCopy[1] = v.y;
		return xCopy;

	}

	@Override
	public Vector2f copyToObject(Vector2f v, float[] x) {
		Vector2f vCopy = v;
		if (vCopy == null) {
			vCopy = new Vector2f(0, 0);
		}
		vCopy.x = x[0];
		vCopy.y = x[1];
		return vCopy;
	}

	@Override
	public int variables() {
		return 2;
	}

}