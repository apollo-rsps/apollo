package org.apollo.game.model.entity.path;

import java.util.NoSuchElementException;
import java.util.Optional;

import org.apollo.game.model.Position;

/**
 * A node representing a weighted {@link Position}.
 * 
 * @author Major
 */
final class Node {

	/**
	 * The cost of this node.
	 */
	private int cost;

	/**
	 * Whether or not this node is open.
	 */
	private boolean open = true;

	/**
	 * The parent node of this node.
	 */
	private Optional<Node> parent = Optional.empty();

	/**
	 * The point this node represents.
	 */
	private final Position position;

	/**
	 * Creates the node with the specified {@link Position} and cost.
	 * 
	 * @param position The position.
	 */
	public Node(Position position) {
		this(position, 0);
	}

	/**
	 * Creates the node with the specified {@link Position} and cost.
	 * 
	 * @param position The position.
	 * @param cost The cost of the node.
	 */
	public Node(Position position, int cost) {
		this.position = position;
		this.cost = cost;
	}

	/**
	 * Closes this node.
	 */
	public void close() {
		open = false;
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
	 * Gets the cost of this node.
	 * 
	 * @return The cost.
	 */
	public int getCost() {
		return cost;
	}

	/**
	 * Gets the parent node of this node.
	 * 
	 * @return The parent node.
	 * @throws NoSuchElementException If this node does not have a parent.
	 */
	public Node getParent() {
		return parent.get();
	}

	/**
	 * Gets the {@link Position} this node represents.
	 * 
	 * @return The position.
	 */
	public Position getPosition() {
		return position;
	}

	@Override
	public int hashCode() {
		return position.getX() * 31 + position.getY();
	}

	/**
	 * Returns whether or not this node has a parent node.
	 * 
	 * @return {@code true} if this node has a parent node, otherwise {@code false}.
	 */
	public boolean hasParent() {
		return parent.isPresent();
	}

	/**
	 * Returns whether or not this {@link Node} is open.
	 * 
	 * @return {@code true} if this node is open, otherwise {@code false}.
	 */
	public boolean isOpen() {
		return open;
	}

	/**
	 * Sets the cost of this node.
	 * 
	 * @param cost The cost.
	 */
	public void setCost(int cost) {
		this.cost = cost;
	}

	/**
	 * Sets the parent node of this node.
	 * 
	 * @param parent The parent node. May be {@code null}.
	 */
	public void setParent(Node parent) {
		this.parent = Optional.ofNullable(parent);
	}

	@Override
	public String toString() {
		return Node.class.getSimpleName() + " [x=" + position.getX() + ", y=" + position.getY() + ", open=" + open + ", cost="
				+ cost + "]";
	}

}