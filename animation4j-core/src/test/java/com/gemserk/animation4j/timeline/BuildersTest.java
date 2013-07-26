package com.gemserk.animation4j.timeline;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import org.hamcrest.core.IsEqual;
import org.junit.Test;

import com.gemserk.animation4j.FloatValue;
import com.gemserk.animation4j.FloatValueConverter;

public class BuildersTest {

	@Test
	public void testAnimationBuilder() {
		FloatValue floatValue1 = new FloatValue(0f);
		FloatValue floatValue2 = new FloatValue(0f);

		TimelineAnimation timelineAnimation = Builders.animation( //
				Builders.timeline() //
						.value(Builders.timelineValue(floatValue1, new FloatValueConverter()) //
								.keyFrame(0f, new FloatValue(500f)) //
								.keyFrame(0.5f, new FloatValue(12500f)) //
								.keyFrame(1f, new FloatValue(500f)) //
						) //
						.value(Builders.timelineValue(floatValue2, new FloatValueConverter()) //
								.keyFrame(0f, new FloatValue(3f)) //
								.keyFrame(0.5f, new FloatValue(10f)) //
								.keyFrame(1f, new FloatValue(50f)) //
						)) //
				.speed(2f) //
				.delay(0.2f) //
				.started(true) //
				.build();
		assertThat(Float.valueOf(timelineAnimation.getDelay()), IsEqual.equalTo(Float.valueOf(0.2f)));
		assertThat(Float.valueOf(timelineAnimation.getDuration()), IsEqual.equalTo(Float.valueOf(1.2f)));
		assertThat(Boolean.valueOf(timelineAnimation.isStarted()), IsEqual.equalTo(Boolean.FALSE));
	}

	@Test
	public void testBuildAnimationWithNoMutableObjects() {
		FloatValue floatValue1 = new FloatValue(0f);

		TimelineAnimation timelineAnimation = Builders.animation( //
				Builders.timeline() //
						.value(Builders.timelineValue(new FloatValueConverter()) //
								.keyFrame(0f, new FloatValue(500f)) //
								.keyFrame(0.5f, new FloatValue(12500f)) //
								.keyFrame(1f, new FloatValue(500f)) //
						)) //
				.delay(0.2f) //
				.started(true) //
				.build();

		assertNotNull(timelineAnimation);
		timelineAnimation.getTimeline().getTimeLineValue(0).setObject(floatValue1);
		timelineAnimation.update(1.2f);
		
		assertThat(Float.valueOf(floatValue1.value), IsEqual.equalTo(Float.valueOf(500f)));
	}
	
	@Test
	public void bugAnimationShouldNotBeStarted() {
		TimelineAnimation timelineAnimation = Builders.animation( //
				Builders.timeline() //
						.value(Builders.timelineValue(new FloatValueConverter()) //
								.keyFrame(0f, new FloatValue(500f)) //
								.keyFrame(0.5f, new FloatValue(12500f)) //
								.keyFrame(1f, new FloatValue(500f)) //
						)) //
				.build();
		assertThat(Boolean.valueOf(timelineAnimation.isStarted()), IsEqual.equalTo(Boolean.FALSE));
	}
	
}
