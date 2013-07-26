package com.gemserk.animation4j.transitions;

import com.gemserk.animation4j.interpolator.FloatArrayInterpolator;
import com.gemserk.animation4j.interpolator.function.InterpolationFunction;

/**
 * Implementation of transition which works over a fixed size float array.
 * 
 * @author acoppes
 * 
 */
public class TransitionFloatArrayImpl implements Transition<float[]> {

	private final TimeTransition timeTransition = new TimeTransition();

	float[] a, b, x;
	InterpolationFunction[] functions;
	float speed = 1f;

	boolean started;
	boolean finished;

	public void setFunctions(InterpolationFunction... functions) {
		this.functions = functions;
	}

	@Override
	public float getSpeed() {
		return speed;
	}

	@Override
	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public TransitionFloatArrayImpl(int variables) {
		a = new float[variables];
		b = new float[variables];
		x = new float[variables];
	}

	public TransitionFloatArrayImpl(float[] value) {
		a = new float[value.length];
		b = new float[value.length];
		x = new float[value.length];
		startWithFloatArray(value);
	}

	@Override
	public float[] get() {
		return x;
	}

	public float[] getValue() {
		return x;
	}

	@Override
	public void startWithFloatArray(float... t) {
		System.arraycopy(t, 0, a, 0, Math.min(t.length, a.length));
		System.arraycopy(t, 0, x, 0, Math.min(t.length, x.length));
		finished = true;
	}

	@Override
	public void startWithFloatArray(float time, float... value) {
		started = true;
		finished = false;

		System.arraycopy(x, 0, a, 0, x.length);
		System.arraycopy(value, 0, b, 0, Math.min(value.length, b.length));

		timeTransition.start(time);
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public void update(float delta) {
		if (!isStarted() || isFinished()) {
			return;
		}

		timeTransition.update(delta * speed);
		FloatArrayInterpolator.interpolate(a, b, x, timeTransition.get(), functions);

		if (timeTransition.isFinished()) {
			finished = true;
		}
	}

	@Override
	public void setStartingValue(float[] value) {
		System.arraycopy(value, 0, a, 0, Math.min(value.length, a.length));
	}

	@Override
	public void setEndingValue(float[] value) {
		System.arraycopy(value, 0, b, 0, Math.min(value.length, b.length));
	}

	@Override
	public void setObject(float[] t) {
		throw new UnsupportedOperationException("cant modify the mutable object for a transition float array");
	}

	@Override
	public void start(float[] t) {
		startWithFloatArray(t);
	}

	@Override
	public void start(float time, float[] t) {
		startWithFloatArray(time, t);
	}

}
