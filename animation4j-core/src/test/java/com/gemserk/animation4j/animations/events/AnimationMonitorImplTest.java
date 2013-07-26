package com.gemserk.animation4j.animations.events;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.gemserk.animation4j.FloatValue;
import com.gemserk.animation4j.FloatValueConverter;
import com.gemserk.animation4j.animations.Animation;
import com.gemserk.animation4j.animations.MockAnimation;
import com.gemserk.animation4j.timeline.Builders;

public class AnimationMonitorImplTest {

	@Test
	public void onEndShouldNotBeCalledIfAnimationNotFinished() {
		Animation animation = new MockAnimation() {
			{
				setFinished(false);
				setStarted(false);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));
	}

	@Test
	public void onEndShouldBeCalledOnceIfAnimationFinishedAndNeverAgainIfAnimationNotStartedAgain() {
		MockAnimation animation = new MockAnimation() {
			{
				setStarted(false);
				setFinished(false);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setStarted(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setFinished(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));

		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
	}

	@Test
	public void onFinishShouldBeCalledOnlyOnceIfAnimationFinished() {

		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
				setFinished(false);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setFinished(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));
	}

	@Test
	public void onStartShouldBeCalledIfAnimationStartedOnlyOnce() {
		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
		animationEventHandler.onStartCalled = false;

		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
	}

	@Test
	public void onStartShouldBeCalledTwiceIfAnimationReseted() {
		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
		animationEventHandler.onStartCalled = false;

		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));

		animation.setStarted(false);
		animationMonitor.checkAnimationChanges();
		animation.setStarted(true);
		animationEventHandler.onStartCalled = false;
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
	}

	@Test
	public void testAnimationHandlerManagerWithTimelineAnimation() {

		FloatValue floatValue = new FloatValue(0f);

		FloatValueConverter typeConverter = new FloatValueConverter();

		Animation animation = Builders.animation(Builders.timeline() //
				.value(Builders.timelineValue(floatValue, typeConverter) //
						.keyFrame(0f, new FloatValue(100f)) //
						.keyFrame(0.1f, new FloatValue(200f)) //
				) //
				).build();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, new AnimationEventHandler() {
			@Override
			public void onAnimationStarted(AnimationEvent e) {
				System.out.println("animation playing!!");
			}

			@Override
			public void onAnimationFinished(AnimationEvent e) {
				System.out.println("animation stopped!!");
			}
		});
		animationMonitor.checkAnimationChanges();
		animation.resume();
		animationMonitor.checkAnimationChanges();

		animation.update(500);
		animationMonitor.checkAnimationChanges();

		animation.update(500);
		animationMonitor.checkAnimationChanges();

		animation.update(500);
		animationMonitor.checkAnimationChanges();

		animation.update(500);
		animationMonitor.checkAnimationChanges();
	}

	@Test
	public void onIterationChangedShouldBeCalledWhenIterationChanged() {

		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
				setIteration(1);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));
		animation.setIteration(2);
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));

		animationEventHandler.onIterationChangedCalled = false;

		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));

	}

	@Test
	public void shouldNotCallOnIterationChangedAfterAnimationFinished() {

		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
				setFinished(true);
				setIteration(1);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));
		animation.setIteration(2);
		animationMonitor.checkAnimationChanges();
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));

	}

	@Test
	public void shouldCallStartAgainIfAnimationRestarted() {

		MockAnimation animation = new MockAnimation() {
			{
				setStarted(false);
				setFinished(false);
				setIteration(1);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setStarted(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setFinished(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.restart();
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setStarted(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onFinishCalled));

		animation.setFinished(true);
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onStartCalled));
		assertEquals(Boolean.TRUE, Boolean.valueOf(animationEventHandler.onFinishCalled));

	}

	@Test
	public void shouldNotCallIterationChangedOnAnimationRestart() {

		MockAnimation animation = new MockAnimation() {
			{
				setStarted(true);
				setFinished(true);
				setIteration(2);
			}
		};

		MockAnimationEventHandler animationEventHandler = new MockAnimationEventHandler();

		AnimationMonitor animationMonitor = new AnimationMonitor(animation, animationEventHandler);
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));

		animation.restart();
		animationEventHandler.reset();
		animationMonitor.checkAnimationChanges();

		assertEquals(Boolean.FALSE, Boolean.valueOf(animationEventHandler.onIterationChangedCalled));

	}
}
