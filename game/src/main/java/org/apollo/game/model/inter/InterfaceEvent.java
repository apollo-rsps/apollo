package org.apollo.game.model.inter;

/**
 * A class of possible events to enable on dynamic interface components.
 * @author Joshua Filby
 */
@SuppressWarnings("PointlessBitwiseExpression")
public enum InterfaceEvent {

	/**
	 * Marks the component as a pause button.
	 */
	PAUSEBUTTON(1 << 0),

	/**
	 * Enables op1 on the component.
	 */
	IF_BUTTON1(1 << 1),

	/**
	 * Enables op2 on the component.
	 */
	IF_BUTTON2(1 << 2),

	/**
	 * Enables op3 on the component.
	 */
	IF_BUTTON3(1 << 3),

	/**
	 * Enables op4 on the component.
	 */
	IF_BUTTON4(1 << 4),

	/**
	 * Enables op5 on the component.
	 */
	IF_BUTTON5(1 << 5),

	/**
	 * Enables op6 on the component.
	 */
	IF_BUTTON6(1 << 6),

	/**
	 * Enables op7 on the component.
	 */
	IF_BUTTON7(1 << 7),

	/**
	 * Enables op8 on the component.
	 */
	IF_BUTTON8(1 << 8),

	/**
	 * Enables op9 on the component.
	 */
	IF_BUTTON9(1 << 9),

	/**
	 * Enables op10 on the component.
	 */
	IF_BUTTON10(1 << 10),

	/**
	 * Enables the ability to target an item on the ground with the component.
	 */
	OPOBJT(1 << 11),

	/**
	 * Enables the ability to target an NPC with the component.
	 */
	OPNPCT(1 << 12),

	/**
	 * Enables the ability to target an object with the component.
	 */
	OPLOCT(1 << 13),

	/**
	 * Enables the ability to target another player with the component.
	 */
	OPPLAYERT(1 << 14),

	/**
	 * Enabled the ability to target an item in the players inventory with the component.
	 */
	OPHELDT(1 << 15),

	/**
	 * Enables the ability to target another component with the component. Requires {@link #IF_TARGET} to be enabled on the components that
	 * are targetable.
	 */
	IF_BUTTONT(1 << 16),

	/**
	 * Sets the component to be dragged to any parent up to depth of 1.
	 */
	DRAG_DEPTH1(1 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 2.
	 */
	DRAG_DEPTH2(2 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 3.
	 */
	DRAG_DEPTH3(3 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 4.
	 */
	DRAG_DEPTH4(4 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 5.
	 */
	DRAG_DEPTH5(5 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 6.
	 */
	DRAG_DEPTH6(6 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 7.
	 */
	DRAG_DEPTH7(7 << 17),

	/**
	 * Sets the component to be dragged to any parent up to depth of 8.
	 */
	DRAG_TARGET(1 << 20),

	/**
	 * Enables the ability of {@link #IF_BUTTONT} enabled components to target the component.
	 */
	IF_TARGET(1 << 21),

	;

	private int mask;


	InterfaceEvent(int mask) {
		this.mask = mask;
	}

	/**
	 * Gets mask.
	 *
	 * @return the mask
	 */
	public int getMask() {
		return mask;
	}
}
