package com.gemserk.animation4j.examples;

import com.gemserk.animation4j.animations.events.AnimationEvent;
import com.gemserk.animation4j.animations.events.AnimationEventHandler;

public class DumpAnimationStateHandler extends AnimationEventHandler {
	
	private final String name;
	
	public DumpAnimationStateHandler() {
		this("no name");
	}

	public DumpAnimationStateHandler(String name) {
		this.name = name;
	}
	
	@Override
	public void onAnimationStarted(AnimationEvent e) {
		System.out.println("animation " + name + " started");
	}

	@Override
	public void onAnimationFinished(AnimationEvent e) {
		System.out.println("animation " + name + " finished");
	}

	@Override
	public void onIterationChanged(AnimationEvent e) {
		System.out.println("animation " + name + " iteration changed");
	}
}