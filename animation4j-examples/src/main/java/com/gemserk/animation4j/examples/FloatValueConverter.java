package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.converters.TypeConverter;

public class FloatValueConverter implements TypeConverter<FloatValue> {

	@Override
	public int variables() {
		return 1;
	}

	@Override
	public float[] copyFromObject(FloatValue object, float[] x) {
		float[] xCopy = x;
		if (xCopy == null) {
			xCopy = new float[variables()];
		}
		xCopy[0] = object.value;
		return xCopy;
	}

	@Override
	public FloatValue copyToObject(FloatValue object, float[] x) {
		object.value = x[0];
		return object;
	}
	
}