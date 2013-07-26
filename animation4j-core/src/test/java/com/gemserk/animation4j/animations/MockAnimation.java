package com.gemserk.animation4j.animations;


public class MockAnimation implements Animation {

	boolean started;

	boolean finished;

	int iteration;

	int iterations;

	public void setStarted(boolean started) {
		this.started = started;
	}

	public void setFinished(boolean finished) {
		this.finished = finished;
	}

	public void setIteration(int iteration) {
		this.iteration = iteration;
	}

	@Override
	public void resume() {

	}

	@Override
	public void update(float delta) {

	}

	@Override
	public boolean isFinished() {
		return finished;
	}

	@Override
	public boolean isStarted() {
		return started;
	}

	@Override
	public void restart() {
		started = false;
		finished = false;
		iteration = 1;
	}

	@Override
	public void stop() {
		started = false;
		finished = false;
	}

	@Override
	public void start(int iterationCount) {
		iterations = iterationCount;
		iteration = 1;
	}

	@Override
	public void pause() {

	}

	@Override
	public void start() {

	}

	@Override
	public int getIteration() {
		return iteration;
	}

	@Override
	public void start(int iterationCount, boolean alternateDirection) {
		start(iterationCount);
	}

	PlayingDirection playingDirection = PlayingDirection.Normal;

	public void setPlayingDirection(PlayingDirection playingDirection) {
		this.playingDirection = playingDirection;
	}

	@Override
	public PlayingDirection getPlayingDirection() {
		return playingDirection;
	}

	@Override
	public void setSpeed(float speed) {
		// TODO Auto-generated function stub
		
	}

	@Override
	public float getSpeed() {
		// TODO Auto-generated function stub
		return 0;
		
	}

	@Override
	public boolean isPlaying() {
		// TODO Auto-generated function stub
		return false;
		
	}

	@Override
	public float getCurrentTime() {
		// TODO Auto-generated function stub
		return 0;
		
	}

}