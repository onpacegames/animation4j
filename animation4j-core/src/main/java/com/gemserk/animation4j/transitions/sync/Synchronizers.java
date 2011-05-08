package com.gemserk.animation4j.transitions.sync;

import java.lang.reflect.Method;

import com.gemserk.animation4j.converters.Converters;
import com.gemserk.animation4j.converters.TypeConverter;
import com.gemserk.animation4j.reflection.ReflectionUtils;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.Transitions;
import com.gemserk.animation4j.transitions.event.TransitionEventHandler;
import com.gemserk.animation4j.transitions.event.TransitionMonitorBuilder;

public class Synchronizers {

	private static SynchronizedTransitionManager synchronizedTransitionManager = new SynchronizedTransitionManager();
	
	private static TransitionHandlersManager transitionHandlersManager = new TransitionHandlersManager();

	private static TransitionMonitorBuilder transitionMonitorBuilder = new TransitionMonitorBuilder();

	/**
	 * Starts a transition and a synchronizer to modify the specified object's field through the transition.
	 * @param object The container of the field to be modified.
	 * @param field The name of the field which contains the object to be modified.
	 * @param startValue The start value of the transition.
	 * @param endValue The end value of the transition.
	 * @param duration The duration of the transition.
	 */
	public static void transition(Object object, String field, Object startValue, Object endValue, int duration) {
		Transition<Object> transition = Transitions.transition(startValue);
		transition.set(endValue, duration);
		transition(object, field, transition);
	}

	/**
	 * Starts a transition and a synchronizer to modify the specified object's field through the transition.
	 * @param object The container of the field to be modified.
	 * @param field The name of the field which contains the object to be modified.
	 * @param endValue The end value of the transition.
	 * @param duration The duration of the transition.
	 */
	public static void transition(Object object, String field, Object endValue, int duration) {

		// this method seem strange here, why making all the reflection stuff ?
		
		try {
			String getterName = ReflectionUtils.getGetterName(field);
			Method getterMethod = ReflectionUtils.findMethod(object.getClass(), getterName);

			if (getterMethod == null)
				throw new RuntimeException();

			Object startValue = getterMethod.invoke(object, (Object[]) null);
			transition(object, field, startValue, endValue, duration);
		} catch (Exception e) {
			throw new RuntimeException("get" + ReflectionUtils.capitalize(field) + "() method not found in " + object.getClass(), e);
		}

	}
	
	/**
	 * Starts a transition and a synchronizer to modify the specified object's field through the transition.
	 * @param object The container of the field to be modified.
	 * @param field The name of the field which contains the object to be modified.
	 * @param transition The transition to synchronize with the object field.
	 */
	public static void transition(Object object, String field, Transition transition) {
		synchronizedTransitionManager.handle(new TransitionReflectionObjectSynchronizer(transition, object, field));
	}

	/**
	 * Performs the synchronization of all the objects with the corresponding transitions registered by calling transition() method.
	 */
	public static void synchronize() {
		synchronizedTransitionManager.synchronize();
		transitionHandlersManager.update();
	}

	/**
	 * Starts a transition and a synchronizer of the transition current value with the specified object. The object must be <b>mutable</b> in order to be modified.
	 * 
	 * @param object The <b>mutable</b> object to be modified in through the transition. 
	 * @param endValue The end value of the transition.
	 * @param duration The duration of the transition.
	 */
	public static void transition(final Object object, Object endValue, int duration) {
		final Transition<Object> transition = Transitions.transition(object);
		transition.set(endValue, duration);
		transition(object, transition);
	}

	/**
	 * Starts a transition and a synchronizer of the transition current value with the specified object. The object must be <b>mutable</b> in order to be modified.
	 * 
	 * @param object The <b>mutable</b> object to be modified in through the transition. 
	 * @param transition The transition to use to modify the object.
	 */
	public static void transition(final Object object, final Transition transition) {

		synchronizedTransitionManager.handle(new TransitionObjectSynchronizer() {

			@Override
			public void synchronize() {
				TypeConverter typeConverter = Converters.converter(object.getClass());
				Object currentValue = transition.get();
				float[] x = typeConverter.copyFromObject(currentValue, null);
				typeConverter.copyToObject(object, x);
			}

			@Override
			public boolean isFinished() {
				return !transition.isTransitioning();
			}

		});

	}
	
	public static void transition(Object object, Transition transition, TransitionEventHandler transitionEventHandler) {
		transition(object, transition);
		transitionHandlersManager.handle(transitionMonitorBuilder.with(transitionEventHandler).monitor(transition).build());
	}
	
	/**
	 * Returns a monitor builder to use to create a 
	 * @return
	 */
	public static TransitionMonitorBuilder monitorBuilder() {
		return transitionMonitorBuilder;
	}

}