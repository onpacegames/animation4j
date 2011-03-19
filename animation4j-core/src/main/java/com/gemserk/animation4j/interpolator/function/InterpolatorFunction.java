package com.gemserk.animation4j.interpolator.function;

public interface InterpolatorFunction {

	/**
	 * @param t
	 *            A real number in the interval [0,1]
	 * @return The interpolated value.
	 */
	float interpolate(float t);

}