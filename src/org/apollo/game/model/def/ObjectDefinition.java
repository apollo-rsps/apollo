package org.apollo.game.model.def;

import org.apollo.game.model.entity.GameObject;

/**
 * Represents a type of {@link GameObject}.
 * 
 * @author Major
 */
public final class ObjectDefinition {

	/**
	 * The array of game object definitions.
	 */
	private static ObjectDefinition[] definitions;

	/**
	 * Gets the total number of object definitions.
	 * 
	 * @return The count.
	 */
	public static int count() {
		return definitions.length;
	}

	/**
	 * Gets the array of object definitions.
	 * 
	 * @return The definitions.
	 */
	public static ObjectDefinition[] getDefinitions() {
		return definitions;
	}

	/**
	 * Initialises the object definitions.
	 * 
	 * @param definitions The decoded definitions.
	 * @throws RuntimeException If there is an id mismatch.
	 */
	public static void init(ObjectDefinition[] definitions) {
		ObjectDefinition.definitions = definitions;
		for (int id = 0; id < definitions.length; id++) {
			ObjectDefinition def = definitions[id];
			if (def.getId() != id) {
				throw new RuntimeException("Item definition id mismatch.");
			}
		}
	}

	/**
	 * Gets the object definition for the specified id.
	 * 
	 * @param id The id of the object.
	 * @return The definition.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public static ObjectDefinition lookup(int id) {
		if (id < 0 || id > definitions.length) {
			throw new IndexOutOfBoundsException(ObjectDefinition.class.getName() + " lookup index " + id
					+ " out of bounds.");
		}
		return definitions[id];
	}

	/**
	 * The object's description.
	 */
	private String description;

	/**
	 * This object's height.
	 */
	private int height;

	/**
	 * The object's id.
	 */
	private final int id;

	/**
	 * Denotes whether this object is impenetrable or not.
	 */
	private boolean impenetrable;

	/**
	 * Denotes whether this object has actions associated with it or not.
	 */
	private boolean interactive;

	/**
	 * The object's menu actions.
	 */
	private String[] menuActions;

	/**
	 * The object's name.
	 */
	private String name;

	/**
	 * Denotes whether the object can be walked over or not.
	 */
	private boolean solid;

	/**
	 * This object's width.
	 */
	private int width;

	/**
	 * Creates a new object definition.
	 * 
	 * @param id The id of the object.
	 */
	public ObjectDefinition(int id) {
		this.id = id;
	}

	/**
	 * Gets the description of this object.
	 * 
	 * @return The description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the height of this object.
	 * 
	 * @return The height.
	 */
	public int getHeight() {
		return height;
	}

	/**
	 * Gets the id of this object.
	 * 
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the menu actions of this object.
	 * 
	 * @return The menu actions.
	 */
	public String[] getMenuActions() {
		return menuActions;
	}

	/**
	 * Gets the name of this object.
	 * 
	 * @return The name.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the with of this object.
	 * 
	 * @return The width.
	 */
	public int getWidth() {
		return width;
	}

	/**
	 * Indicates the impenetrability of this object.
	 * 
	 * @return {@code true} if this object is impenetrable, otherwise {@code false}.
	 */
	public boolean isImpenetrable() {
		return impenetrable;
	}

	/**
	 * Indicates the interactivity of this object.
	 * 
	 * @return {@code true} if the object is interactive, otherwise {@code false}.
	 */
	public boolean isInteractive() {
		return interactive;
	}

	/**
	 * Indicates the solidity of this object.
	 * 
	 * @return {@code true} if this object is solid, otherwise {@code false}.
	 */
	public boolean isSolid() {
		return solid;
	}

	/**
	 * Sets the description of this object.
	 * 
	 * @param description The description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * Sets the height of this object.
	 * 
	 * @param height The height.
	 */
	public void setHeight(int height) {
		this.height = height;
	}

	/**
	 * Sets the impenetrability of this object.
	 * 
	 * @param impenetrable The impenetrability.
	 */
	public void setImpenetrable(boolean impenetrable) {
		this.impenetrable = impenetrable;
	}

	/**
	 * Sets the interactivity of this object.
	 * 
	 * @param interactive The interactivity.
	 */
	public void setInteractive(boolean interactive) {
		this.interactive = interactive;
	}

	/**
	 * Sets the menu actions of this object.
	 * 
	 * @param menuActions The menu actions.
	 */
	public void setMenuActions(String[] menuActions) {
		this.menuActions = menuActions;
	}

	/**
	 * Sets the name of this object.
	 * 
	 * @param name The name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Sets the solidity of this object.
	 * 
	 * @param solid The solidity.
	 */
	public void setSolid(boolean solid) {
		this.solid = solid;
	}

	/**
	 * Sets the width of this object.
	 * 
	 * @param width The width.
	 */
	public void setWidth(int width) {
		this.width = width;
	}

}