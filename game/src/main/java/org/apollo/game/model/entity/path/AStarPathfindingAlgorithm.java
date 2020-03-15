package org.apollo.game.model.entity.path;

import org.apollo.game.model.Direction;
import org.apollo.game.model.Position;
import org.apollo.game.model.area.collision.CollisionManager;

import java.util.*;

/**
 * A {@link PathfindingAlgorithm} that utilises the A* algorithm to find a solution.
 * <p>
 * This implementation utilises a {@link PriorityQueue} of open {@link Node}s, in addition to the usual {@link HashSet}.
 * This allows for logarithmic-time finding of the cheapest element (as opposed to the linear time associated with
 * iterating over the set), whilst still maintaining the constant time contains and remove of the set.
 * <p>
 * This implementation also avoids the linear-time removal from the queue by polling until the first open node is found
 * when identifying the cheapest node.
 *
 * @author Major
 */
public final class AStarPathfindingAlgorithm extends PathfindingAlgorithm {

	/**
	 * The Heuristic used by this PathfindingAlgorithm.
	 */
	private final Heuristic heuristic;

	/**
	 * Creates the A* pathfinding algorithm with the specified {@link Heuristic}.
	 *
	 * @param collisionManager The {@link CollisionManager} used to check if there is a collision
	 * between two {@link Position}s in a path.
	 * @param heuristic The Heuristic.
	 */
	public AStarPathfindingAlgorithm(CollisionManager collisionManager, Heuristic heuristic) {
		super(collisionManager);
		this.heuristic = heuristic;
	}

	@Override
	public Deque<Position> find(Position origin, Position target) {
		Map<Position, Node> nodes = new HashMap<>();
		Node start = new Node(origin), end = new Node(target);
		nodes.put(origin, start);
		nodes.put(target, end);

		Set<Node> open = new HashSet<>();
		Queue<Node> sorted = new PriorityQueue<>();
		open.add(start);
		sorted.add(start);

		do {
			Node active = getCheapest(sorted);
			Position position = active.getPosition();

			if (position.equals(target)) {
				break;
			}

			open.remove(active);
			active.close();

			int x = position.getX(), y = position.getY();
			for (int nextX = x - 1; nextX <= x + 1; nextX++) {
				for (int nextY = y - 1; nextY <= y + 1; nextY++) {
					if (nextX == x && nextY == y) {
						continue;
					}

					Position adjacent = new Position(nextX, nextY, position.getHeight());
					Direction direction = Direction.between(adjacent, position);
					if (traversable(adjacent, direction)) {
						Node node = nodes.computeIfAbsent(adjacent, Node::new);
						compare(active, node, open, sorted, heuristic);
					}
				}
			}
		} while (!open.isEmpty());

		Deque<Position> shortest = new ArrayDeque<>();
		Node active = end;

		if (active.hasParent()) {
			Position position = active.getPosition();

			while (!origin.equals(position)) {
				shortest.addFirst(position);
				active = active.getParent(); // If the target has a parent then all of the others will.
				position = active.getPosition();
			}
		}

		return shortest;
	}

	/**
	 * Compares the two specified {@link Node}s, adding the other node to the open {@link Set} if the estimation is
	 * cheaper than the current cost.
	 *
	 * @param active The active node.
	 * @param other The node to compare the active node against.
	 * @param open The set of open nodes.
	 * @param sorted The sorted {@link Queue} of nodes.
	 * @param heuristic The {@link Heuristic} used to estimate the cost of the node.
	 */
	private void compare(Node active, Node other, Set<Node> open, Queue<Node> sorted, Heuristic heuristic) {
		int cost = active.getCost() + heuristic.estimate(active.getPosition(), other.getPosition());

		if (other.getCost() > cost) {
			open.remove(other);
			other.close();
		} else if (other.isOpen() && !open.contains(other)) {
			other.setCost(cost);
			other.setParent(active);
			open.add(other);
			sorted.add(other);
		}
	}

	/**
	 * Gets the cheapest open {@link Node} from the {@link Queue}.
	 *
	 * @param nodes The queue of nodes.
	 * @return The cheapest node.
	 */
	private Node getCheapest(Queue<Node> nodes) {
		Node node = nodes.peek();
		while (!node.isOpen()) {
			nodes.poll();
			node = nodes.peek();
		}

		return node;
	}

}