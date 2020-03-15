package org.apollo.game.scheduling.impl;

import com.google.common.base.Preconditions;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionManager;
import org.apollo.game.model.entity.Npc;
import org.apollo.game.model.entity.WalkingQueue;
import org.apollo.game.model.entity.path.SimplePathfindingAlgorithm;
import org.apollo.game.scheduling.ScheduledTask;

import java.util.*;

/**
 * A {@link ScheduledTask} that causes {@link Npc}s to randomly walk around in their boundary.
 *
 * @author Major
 */
public final class NpcMovementTask extends ScheduledTask {

	/**
	 * The delay between executions of this task, in pulses.
	 */
	private static final int DELAY = 5;

	/**
	 * The random number generator used to calculate how many Npcs should be moved per execution.
	 */
	private static final Random RANDOM = new Random();

	/**
	 * The comparator used to sort the Npcs in the PriorityQueue.
	 */
	private static final Comparator<Npc> RANDOM_COMPARATOR = (first, second) -> RANDOM.nextInt(2) - 1;

	/**
	 * The PathfindingAlgorithm used by this Task.
	 */
	private final SimplePathfindingAlgorithm algorithm;

	/**
	 * The Queue of Npcs.
	 */
	private final Queue<Npc> npcs = new PriorityQueue<>(RANDOM_COMPARATOR);

	/**
	 * Creates the NpcMovementTask.
	 *
	 * @param collisionManager The {@link CollisionManager} used to check if an {@link Npc} movement is valid.
	 */
	public NpcMovementTask(CollisionManager collisionManager) {
		super(DELAY, false);
		algorithm = new SimplePathfindingAlgorithm(collisionManager);
	}

	/**
	 * Adds the {@link Npc} to this {@link ScheduledTask}.
	 *
	 * @param npc The Npc to add.
	 */
	public void addNpc(Npc npc) {
		Preconditions.checkArgument(npc.hasBoundaries(), "Cannot add an npc with no boundaries to the NpcMovementTask.");
		npcs.offer(npc);
	}

	@Override
	public void execute() {
		int count = RANDOM.nextInt(npcs.size() / 50 + 5);
		for (int iterations = 0; iterations < count; iterations++) {
			Npc npc = npcs.poll();
			if (npc == null) {
				break;
			}

			Position[] boundary = npc.getBoundaries().get();
			Position current = npc.getPosition();
			Position min = boundary[0], max = boundary[1];
			int currentX = current.getX(), currentY = current.getY();

			boolean negativeX = RANDOM.nextBoolean(), negativeY = RANDOM.nextBoolean();
			int x = RANDOM.nextInt(negativeX ? currentX - min.getX() : max.getX() - currentX);
			int y = RANDOM.nextInt(negativeY ? currentY - min.getY() : max.getY() - currentY);

			int dx = negativeX ? -x : x;
			int dy = negativeY ? -y : y;
			Position next = new Position(currentX + dx, currentY + dy);

			Deque<Position> positions = algorithm.find(current, next, boundary);
			WalkingQueue queue = npc.getWalkingQueue();

			Position first = positions.pollFirst();

			if (first != null) {
				queue.addFirstStep(first);
				positions.forEach(queue::addStep);
			}

			npcs.offer(npc);
		}
	}

}