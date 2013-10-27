package org.apollo.game.model;

/**
 * Represents a 'still graphic'.
 * @author Graham
 */
public final class Graphic {

	/**
	 * A special graphic which stops the current graphic.
	 */
	public static final Graphic STOP_GRAPHIC = new Graphic(-1);

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * The height.
	 */
	private final int height;

	/**
	 * Creates a new graphic with no delay and a height of zero.
	 * @param id The id.
	 */
	public Graphic(int id) {
		this(id, 0, 0);
	}

	/**
	 * Creates a new graphic with a height of zero.
	 * @param id The id.
	 * @param delay The delay.
	 */
	public Graphic(int id, int delay) {
		this(id, delay, 0);
	}

	/**
	 * Creates a new graphic.
	 * @param id The id.
	 * @param delay The delay.
	 * @param height The height.
	 */
	public Graphic(int id, int delay, int height) {
		this.id = id;
		this.delay = delay;
		this.height = height;
	}

	/**
	 * Gets the graphic's id.
	 * @return The graphic's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the graphic's delay.
	 * @return The graphic's delay.
	 */
	public int getDelay() {
		return delay;
	}

	/**
	 * Gets the graphic's height.
	 * @return The graphic's height.
	 */
	public int getHeight() {
		return height;
	}

}
