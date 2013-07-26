package com.gemserk.animation4j.time;

/**
 * Default implementation of TimeProvider using system timer.
 * 
 * @author acoppes
 */
public class SystemTimeProvider implements TimeProvider {

	@Override
	public float getTime() {
		return System.nanoTime() * 0.000000001f;
	}
}