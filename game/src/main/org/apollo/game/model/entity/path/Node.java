package org.apollo.game.model.entity.path;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apollo.game.model.Position;

import com.google.common.base.MoreObjects;

/**
 * A Node representing a weighted {@link Position}.
 *
 * @author Major
 */
final class Node implements Comparable<Node> {

	/**
	 * The cost of this Node.
	 */
	private int cost;

	/**
	 * Whether or not this Node is open.
	 */
	private boolean open = true;

	/**
	 * The parent Node of this Node.
	 */
	private Optional<Node> parent = Optional.empty();

	/**
	 * The Position of this Node.
	 */
	private final Position position;

	/**
	 * Creates the Node with the specified {@link Position} and cost.
	 *
	 * @param position The Position.
	 */
	public Node(Position position) {
		this(position, 0);
	}

	/**
	 * Creates the Node with the specified {@link Position} and cost.
	 *
	 * @param position The Position.
	 * @param cost The cost of the Node.
	 */
	public Node(Position position, int cost) {
		this.position = position;
		this.cost = cost;
	}

	/**
	 * Closes this Node.
	 */
	public void close() {
		open = false;
	}

	@Override
	public int compareTo(Node other) {
		return Integer.compare(cost, other.cost);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Node) {
			Node other = (Node) obj;

			return position.equals(other.position);
		}

		return false;
	}

	/**
	 * Gets the cost of this Node.
	 *
	 * @return The cost.
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Gets the parent Node of this Node.
	 *
	 * @return The parent Node.
	 * @throws NoSuchElementException If this Node does not have a parent.
	 */
	public Node getParent() {
		return parent.get();
	}

	/**
	 * Gets the {@link Position} this Node represents.
	 *
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		return position.hashCode();
	}

	/**
	 * Returns whether or not this Node has a parent Node.
	 *
	 * @return {@code true} if this Node has a parent Node, otherwise {@code false}.
	 */
	public boolean hasParent() {
		return parent.isPresent();
	}

	/**
	 * Returns whether or not this {@link Node} is open.
	 *
	 * @return {@code true} if this Node is open, otherwise {@code false}.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Sets the cost of this Node.
	 *
	 * @param cost The cost.
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * Sets the parent Node of this Node.
	 *
	 * @param parent The parent Node. May be {@code null}.
	 */
	public void setParent(Node parent) {
		this.parent = Optional.ofNullable(parent);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this).add("position", position).add("open", open).add("cost", cost)
				.toString();
	}

}