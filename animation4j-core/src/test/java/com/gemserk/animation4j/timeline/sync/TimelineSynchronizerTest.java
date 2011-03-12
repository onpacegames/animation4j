package com.gemserk.animation4j.timeline.sync;

import org.jmock.Expectations;
import org.jmock.Mockery;
import org.jmock.integration.junit4.JMock;
import org.jmock.lib.legacy.ClassImposteriser;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.gemserk.animation4j.timeline.TimelineIterator;
import com.gemserk.animation4j.timeline.TimelineValue;
import com.gemserk.animation4j.timeline.sync.ObjectSynchronizer;
import com.gemserk.animation4j.timeline.sync.TimelineSynchronizer;



@RunWith(JMock.class)
public class TimelineSynchronizerTest {

	Mockery mockery = new Mockery() {
		{
			setImposteriser(ClassImposteriser.INSTANCE);
		}
	};
	
	@Test
	@SuppressWarnings("rawtypes")
	public void shouldGetValuesFromIteratorAndSetToObject() {
		
		final TimelineIterator timelineIterator = mockery.mock(TimelineIterator.class);
		final TimelineValue timelineValue = mockery.mock(TimelineValue.class);
		final ObjectSynchronizer objectSynchronizer = mockery.mock(ObjectSynchronizer.class);
		
		mockery.checking(new Expectations() {
			{
				oneOf(timelineIterator).hasNext();
				will(returnValue(true));
				
				oneOf(timelineIterator).next();
				will(returnValue(timelineValue));
				
				oneOf(timelineValue).getName();
				will(returnValue("x"));
				
				oneOf(timelineValue).getValue(10);
				will(returnValue(100f));
				
				oneOf(objectSynchronizer).setValue("x", 100f);
				
				oneOf(timelineIterator).hasNext();
				will(returnValue(false));
			}
		});
		
		TimelineSynchronizer timelineSynchronizer = new TimelineSynchronizer(objectSynchronizer);
		
		timelineSynchronizer.syncrhonize(timelineIterator,  10);
		
	}
	
}