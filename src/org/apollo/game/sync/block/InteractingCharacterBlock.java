package org.apollo.game.sync.block;

/**
 * The InteractingCharacterBlock {@link SynchronizationBlock}.
 * 
 * @note As all Apollo events should be immutable to avoid concurency issues, this uses the index of the character
 *       rather than the actual character. This should not be changed.
 * 
 * @author Major
 */
public class InteractingCharacterBlock extends SynchronizationBlock {

	/**
	 * The index of the character.
	 */
	private final int characterIndex;

	/**
	 * Creates the interacting character block.
	 * 
	 * @param characterIndex The index of the current interacting character.
	 */
	public InteractingCharacterBlock(int characterIndex) {
		this.characterIndex = characterIndex;
	}

	/**
	 * Gets the interacting character's current index.
	 * 
	 * @return The index of the character.
	 */
	public int getInteractingCharacterIndex() {
		return characterIndex;
	}

}