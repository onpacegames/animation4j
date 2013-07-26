package com.gemserk.animation4j.interpolator;

import com.gemserk.animation4j.interpolator.function.InterpolationFunction;
import com.gemserk.animation4j.interpolator.function.InterpolationFunctions;

public class FloatInterpolator {
	
	public static float interpolate(float a, float b, float t) {
		return interpolate(a, b, t, InterpolationFunctions.linear());
	}
	
	public static float interpolate(float a, float b, float t, InterpolationFunction function) {
		float tCopy = t;
		tCopy = function.interpolate(tCopy);
		return a * (1 - tCopy) + b * tCopy;
	}

}