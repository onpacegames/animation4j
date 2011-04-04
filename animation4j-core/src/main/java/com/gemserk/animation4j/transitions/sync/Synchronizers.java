package com.gemserk.animation4j.transitions.sync;

import java.lang.reflect.Method;

import com.gemserk.animation4j.converters.Converters;
import com.gemserk.animation4j.converters.TypeConverter;
import com.gemserk.animation4j.reflection.ReflectionUtils;
import com.gemserk.animation4j.transitions.Transition;
import com.gemserk.animation4j.transitions.Transitions;

public class Synchronizers {

	static SynchronizedTransitionManager synchronizedTransitionManager = new SynchronizedTransitionManager();

	public static void transition(Object object, String field, Object startValue, Object endValue, int time) {

		Transition<Object> transition = Transitions.transition(startValue);
		transition.set(endValue, time);

		synchronizedTransitionManager.handle(new TransitionReflectionObjectSynchronizer(transition, object, field));

	}

	public static void transition(Object object, String field, Object endValue, int time) {

		try {
			String getterName = ReflectionUtils.getGetterName(field);
			Method getterMethod = ReflectionUtils.findMethod(object.getClass(), getterName);
			
			if (getterMethod == null)
				throw new RuntimeException();
			
			Object startValue = getterMethod.invoke(object, (Object[]) null);
			transition(object, field, startValue, endValue, time);
		} catch (Exception e) {
			throw new RuntimeException("get" + ReflectionUtils.capitalize(field) + "() method not found in " + object.getClass(), e);
		}

	}

	public static void synchronize() {
		synchronizedTransitionManager.synchronize();
	}

	public static void transition(final Object object, Object endValue, int time) {
		final Transition<Object> transition = Transitions.transition(object);
		transition.set(endValue, time);
		transition(object, transition);
	}
	
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

}