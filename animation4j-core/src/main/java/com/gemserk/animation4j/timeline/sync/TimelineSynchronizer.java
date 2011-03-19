package com.gemserk.animation4j.timeline.sync;

import com.gemserk.animation4j.timeline.TimelineIterator;
import com.gemserk.animation4j.timeline.TimelineValue;

/**
 * Synchronizes all values of a time line with an object using the specified ObjectSynchronizer.
 * 
 * @author acoppes
 */
public class TimelineSynchronizer {

	private final ObjectSynchronizer objectSynchronizer;

	/**
	 * @param objectSynchronizer
	 *            The ObjectSynchronizer to use to synchronize the time line values.
	 */
	public TimelineSynchronizer(ObjectSynchronizer objectSynchronizer) {
		this.objectSynchronizer = objectSynchronizer;
	}

	/**
	 * Synchronizes the values of the time line with the current object.
	 * 
	 * @param timelineIterator
	 *            The time line iterator to iterate over the elements and synchronze each one.
	 * @param time
	 *            The time to be used when getting the current value of each element to be synchronized.
	 */
	public void syncrhonize(TimelineIterator timelineIterator, float time) {
		while (timelineIterator.hasNext()) {
			TimelineValue<Object> timelineValue = timelineIterator.next();
			String name = timelineValue.getName();
			Object value = timelineValue.getValue(time);
			objectSynchronizer.setValue(name, value);
		}
	}

}