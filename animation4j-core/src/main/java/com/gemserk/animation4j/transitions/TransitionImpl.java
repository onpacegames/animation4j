package com.gemserk.animation4j.transitions;

import com.gemserk.animation4j.converters.TypeConverter;
import com.gemserk.animation4j.interpolator.function.InterpolationFunction;

/**
 * Default implementation of Transition interface which works over a mutable object by using a TypeConverter.
 * 
 * @author acoppes
 * 
 */
public class TransitionImpl<T> implements Transition<T> {

	T mutableObject;
	TypeConverter<T> typeConverter;

	TransitionFloatArrayImpl transition;

	float[] tmp;

	public void setFunctions(InterpolationFunction... functions) {
		transition.setFunctions(functions);
	}

	@Override
	public float getSpeed() {
		return transition.getSpeed();
	}

	@Override
	public void setSpeed(float speed) {
		transition.setSpeed(speed);
	}

	public TransitionImpl(T mutableObject, TypeConverter<T> typeConverter) {
		this.mutableObject = mutableObject;
		this.typeConverter = typeConverter;
		transition = new TransitionFloatArrayImpl(typeConverter.variables());
		if (mutableObject != null) {
			tmp = typeConverter.copyFromObject(mutableObject, tmp);
		} else {
			tmp = new float[typeConverter.variables()];
		}
		transition.startWithFloatArray(tmp);
	}
	
	public TransitionImpl(TypeConverter<T> typeConverter) {
		this(null, typeConverter);
	}

	/**
	 * Sets the object to be modified by the transition.
	 * 
	 * @param object
	 *            The mutable object to be modified.
	 */
	@Override
	public void setObject(T object) {
		this.mutableObject = object;
	}

	@Override
	public T get() {
		return mutableObject;
	}

	public float[] getValue() {
		return transition.get();
	}

	@Override
	public void start(T t) {
		typeConverter.copyFromObject(t, tmp);
		startWithFloatArray(tmp);
	}

	@Override
	public void start(float time, T t) {
		typeConverter.copyFromObject(this.mutableObject, transition.x);
		typeConverter.copyFromObject(t, tmp);
		transition.startWithFloatArray(time, tmp);
	}

	@Override
	public void startWithFloatArray(float... value) {
		transition.startWithFloatArray(value);
		typeConverter.copyToObject(mutableObject, transition.get());
	}

	@Override
	public void startWithFloatArray(float time, float... value) {
		transition.startWithFloatArray(time, value);
	}

	@Override
	public boolean isStarted() {
		return transition.isStarted();
	}

	@Override
	public boolean isFinished() {
		return transition.isFinished();
	}

	@Override
	public void update(float delta) {
		if (!isStarted() || isFinished()) {
			return;
		}
		transition.update(delta);
		typeConverter.copyToObject(mutableObject, transition.get());
	}

	@Override
	public void setStartingValue(T t) {
		transition.setStartingValue(typeConverter.copyFromObject(t, tmp));
	}

	@Override
	public void setStartingValue(float[] value) {
		transition.setStartingValue(value);
	}

	@Override
	public void setEndingValue(T t) {
		transition.setEndingValue(typeConverter.copyFromObject(t, tmp));
	}

	@Override
	public void setEndingValue(float[] value) {
		transition.setEndingValue(value);
	}

}
