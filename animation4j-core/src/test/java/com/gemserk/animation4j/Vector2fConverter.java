package com.gemserk.animation4j;

import com.gemserk.animation4j.converters.TypeConverter;

public class Vector2fConverter implements TypeConverter<Vector2f> {
	
	public static int arrayInstances = 0;

	@Override
	public float[] copyFromObject(Vector2f v, float[] x) {
		float[] xCopy = x;
		if (xCopy == null) {
			xCopy = new float[variables()];
			arrayInstances++;
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