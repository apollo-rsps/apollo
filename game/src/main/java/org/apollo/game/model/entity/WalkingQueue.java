package org.apollo.game.model.entity;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.World;
import org.apollo.game.model.area.Region;
import org.apollo.game.model.area.RegionRepository;
import org.apollo.game.model.area.collision.CollisionManager;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Queue;

/**
 * A queue of {@link Direction}s which a {@link Mob} will follow.
 *
 * @author Graham
 */
public final class WalkingQueue {

	/**
	 * The Mob this WalkingQueue belongs to.
	 */
	private final Mob mob;

	/**
	 * The Deque of active points in this WalkingQueue.
	 */
	private final Deque<Position> points = new ArrayDeque<>();

	/**
	 * The Deque of previous points in this WalkingQueue.
	 */
	private final Deque<Position> previousPoints = new ArrayDeque<>();

	/**
	 * The running status of this WalkingQueue.
	 */
	private boolean running;

	/**
	 * Creates the WalkingQueue.
	 *
	 * @param mob The {@link Mob} the WalkingQueue is for.
	 */
	public WalkingQueue(Mob mob) {
		this.mob = mob;
	}

	/**
	 * Adds a first step into this WalkingQueue.
	 *
	 * @param next The {@link Position} of the step.
	 */
	public void addFirstStep(Position next) {
		points.clear();
		running = false;

		/*
		 * We need to connect 'current' and 'next' whilst accounting for the
		 * fact that the client and server might be out of sync (i.e. what the
		 * client thinks is 'current' is different to what the server thinks is
		 * 'current').
		 *
		 * First try to connect them via points from the previous queue.
		 */
		Queue<Position> backtrack = new ArrayDeque<>();

		while (!previousPoints.isEmpty()) {
			Position position = previousPoints.pollLast();
			backtrack.add(position);

			if (position.equals(next)) {
				backtrack.forEach(this::addStep);
				previousPoints.clear();
				return;
			}
		}

		/* If that doesn't work, connect the points directly. */
		previousPoints.clear();
		addStep(next);
	}

	/**
	 * Adds a step to this WalkingQueue.
	 *
	 * @param next The {@link Position} of the step.
	 */
	public void addStep(Position next) {
		Position current = points.peekLast();

		/*
		 * If current equals next, addFirstStep doesn't end up adding anything points queue. This makes peekLast()
		 * return null. If it does, the correct behaviour is to fill it in with mob.getPosition().
		 */
		if (current == null) {
			current = mob.getPosition();
		}

		addStep(current, next);
	}

	/**
	 * Clears this WalkingQueue.
	 */
	public void clear() {
		points.clear();
		running = false;
		previousPoints.clear();
	}

	/**
	 * Returns whether or not this WalkingQueue has running enabled.
	 *
	 * @return {@code true} iff this WalkingQueue has running enabled.
	 */
	public boolean isRunning() {
		return running;
	}

	/**
	 * Pulses this WalkingQueue.
	 */
	public void pulse() {
		Position position = mob.getPosition();
		int height = position.getHeight();

		Direction firstDirection = Direction.NONE;
		Direction secondDirection = Direction.NONE;
		World world = mob.getWorld();
		CollisionManager collisionManager = world.getCollisionManager();

		Position next = points.poll();
		if (next != null) {
			firstDirection = Direction.between(position, next);

			if (!collisionManager.traversable(position, EntityType.NPC, firstDirection)) {
				clear();
				firstDirection = Direction.NONE;
			} else {
				previousPoints.add(next);
				position = new Position(next.getX(), next.getY(), height);
				mob.setLastDirection(firstDirection);

				if (running) {
					next = points.poll();
					if (next != null) {
						secondDirection = Direction.between(position, next);

						if (!collisionManager.traversable(position, EntityType.NPC, secondDirection)) {
							clear();
							secondDirection = Direction.NONE;
						} else {
							previousPoints.add(next);
							position = new Position(next.getX(), next.getY(), height);
							mob.setLastDirection(secondDirection);
						}
					}
				}
			}
		}

		mob.setDirections(firstDirection, secondDirection);
		mob.setPosition(position);
	}

	/**
	 * Sets the running flag status of this WalkingQueue.
	 *
	 * @param running The running flag.
	 */
	public void setRunning(boolean running) {
		this.running = running;
	}

	/**
	 * Gets the size of this WalkingQueue, which is the number of points remaining in it.
	 *
	 * @return The size.
	 */
	public int size() {
		return points.size();
	}

	/**
	 * Adds the {@code next} step to this WalkingQueue.
	 *
	 * @param current The current {@link Position}.
	 * @param next The next Position.
	 */
	private void addStep(Position current, Position next) {
		int nextX = next.getX(), nextY = next.getY(), height = next.getHeight();
		int deltaX = nextX - current.getX();
		int deltaY = nextY - current.getY();

		int max = Math.max(Math.abs(deltaX), Math.abs(deltaY));

		RegionRepository repository = mob.getWorld().getRegionRepository();
		Region region = repository.fromPosition(current);

		for (int count = 0; count < max; count++) {
			if (deltaX < 0) {
				deltaX++;
			} else if (deltaX > 0) {
				deltaX--;
			}

			if (deltaY < 0) {
				deltaY++;
			} else if (deltaY > 0) {
				deltaY--;
			}

			Position step = new Position(nextX - deltaX, nextY - deltaY, height);
			if (!region.contains(step)) {
				region = repository.fromPosition(step);
			}

			points.add(step);
		}
	}

}