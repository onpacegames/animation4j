package com.gemserk.animation4j.timeline;

import com.gemserk.animation4j.converters.TypeConverter;

/**
 * Implementation of TimelineValueInterface which internally modifies a mutable object.
 * 
 * @author acoppes
 * 
 * @param <T>
 *            The type of the value.
 */
public class TimelineValueImpl<T> implements TimelineValue<T> {

	private final float[] x;
	private final TimelineValueFloatArrayImpl timelineValueFloatArrayImpl;

	private TypeConverter<T> typeConverter;

	/**
	 * The object to be modified with the type converter when processing the timeline value.
	 */
	private T object;

	public void setObject(T object) {
		this.object = object;
	}

	public T getObject() {
		return object;
	}

	/**
	 * Creates a new instance which works over the specified mutable object.
	 * 
	 * @param object
	 *            The mutable object to be modified on setTime(time).
	 * @param typeConverter
	 *            The TypeConverter to be used when modifying the object.
	 */
	public TimelineValueImpl(T object, TypeConverter<T> typeConverter) {
		this.object = object;
		this.typeConverter = typeConverter;
		this.x = new float[typeConverter.variables()];
		timelineValueFloatArrayImpl = new TimelineValueFloatArrayImpl(x);
	}

	public void addKeyFrame(KeyFrame keyFrame) {
		timelineValueFloatArrayImpl.addKeyFrame(keyFrame);
	}

	public void setTime(float time) {
		setTime(object, time);
	}

	@Override
	public void setTime(T object, float time) {
		timelineValueFloatArrayImpl.setTime(time);
		typeConverter.copyToObject(object, x);
	}

	@Override
	public String toString() {
		return timelineValueFloatArrayImpl.toString();
	}

}