package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.converters.TypeConverter;

public class ColorConverter implements TypeConverter<Color>{

	@Override
	public int variables() {
		return 4;
	}

	@Override
	public float[] copyFromObject(Color c, float[] x) {
		float[] xCopy = x;
		if (xCopy == null) {
			xCopy = new float[variables()];
		}
		xCopy[0] = c.r;
		xCopy[1] = c.g;
		xCopy[2] = c.b;
		xCopy[3] = c.a;
		return xCopy;
	}

	@Override
	public Color copyToObject(Color c, float[] x) {
		Color colorCopy = c;
		if (colorCopy == null) {
			colorCopy = new Color();
		}
		colorCopy.r = x[0];
		colorCopy.g = x[1];
		colorCopy.b = x[2];
		colorCopy.a = x[3];
		return colorCopy;
	}

}
