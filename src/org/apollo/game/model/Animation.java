package org.apollo.game.model;

/**
 * Represents an animation.
 * @author Graham
 */
public final class Animation {

	/**
	 * A special animation which stops the current animation.
	 */
	public static final Animation STOP_ANIMATION = new Animation(-1);

	/**
	 * The yes animation.
	 */
	public static final Animation YES = new Animation(855);

	/**
	 * The no animation.
	 */
	public static final Animation NO = new Animation(856);

	/**
	 * The thinking animation.
	 */
	public static final Animation THINKING = new Animation(857);

	/**
	 * The bow animation.
	 */
	public static final Animation BOW = new Animation(858);

	/**
	 * The angry animation.
	 */
	public static final Animation ANGRY = new Animation(859);

	/**
	 * The cry animation.
	 */
	public static final Animation CRY = new Animation(860);

	/**
	 * The laugh animation.
	 */
	public static final Animation LAUGH = new Animation(861);

	/**
	 * The cheer animation.
	 */
	public static final Animation CHEER = new Animation(862);

	/**
	 * The wave animation.
	 */
	public static final Animation WAVE = new Animation(863);

	/**
	 * The beckon animation.
	 */
	public static final Animation BECKON = new Animation(864);

	/**
	 * The clap animation.
	 */
	public static final Animation CLAP = new Animation(865);

	/**
	 * The dance animation.
	 */
	public static final Animation DANCE = new Animation(866);

	/**
	 * The panic animation.
	 */
	public static final Animation PANIC = new Animation(2105);

	/**
	 * The jig  animation.
	 */
	public static final Animation JIG = new Animation(2106);

	/**
	 * The spin animation.
	 */
	public static final Animation SPIN = new Animation(2107);

	/**
	 * The head bang animation.
	 */
	public static final Animation HEAD_BANG = new Animation(2108);

	/**
	 * The joy jump animation.
	 */
	public static final Animation JOY_JUMP = new Animation(2109);

	/**
	 * The raspberry animation.
	 */
	public static final Animation RASPBERRY = new Animation(2110);

	/**
	 * The yawn animation.
	 */
	public static final Animation YAWN = new Animation(2111);

	/**
	 * The salute animation.
	 */
	public static final Animation SALUTE = new Animation(2112);

	/**
	 * The shrug animation.
	 */
	public static final Animation SHRUG = new Animation(2113);

	/**
	 * The blow kiss animation.
	 */
	public static final Animation BLOW_KISS = new Animation(1368);

	/**
	 * The glass wall animation.
	 */
	public static final Animation GLASS_WALL = new Animation(1128);

	/**
	 * The lean animation.
	 */
	public static final Animation LEAN = new Animation(1129);

	/**
	 * The climb rope animation.
	 */
	public static final Animation CLIMB_ROPE = new Animation(1130);

	/**
	 * The glass box animation.
	 */
	public static final Animation GLASS_BOX = new Animation(1131);

	/**
	 * The goblin bow animation.
	 */
	public static final Animation GOBLIN_BOW = new Animation(2127);

	/**
	 * The goblin dance animation.
	 */
	public static final Animation GOBLIN_DANCE = new Animation(2128);

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The delay.
	 */
	private final int delay;

	/**
	 * Creates a new animation with no delay.
	 * @param id The id.
	 */
	public Animation(int id) {
		this(id, 0);
	}

	/**
	 * Creates a new animation.
	 * @param id The id.
	 * @param delay The delay.
	 */
	public Animation(int id, int delay) {
		this.id = id;
		this.delay = delay;
	}

	/**
	 * Gets the animation's id.
	 * @return The animation's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the animation's delay.
	 * @return The animation's delay.
	 */
	public int getDelay() {
		return delay;
	}

}
