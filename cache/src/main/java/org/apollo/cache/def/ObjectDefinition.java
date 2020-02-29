package org.apollo.cache.def;

import com.google.common.base.Preconditions;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;

/**
 * Represents a type of GameObject.
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
		Preconditions.checkElementIndex(id, definitions.length, "Id out of bounds.");
		return definitions[id];
	}

	/**
	 * The object's description.
	 */
	private String description;

	/**
	 * The object's id.
	 */
	private final int id;

	/**
	 * Denotes whether this object is impenetrable or not.
	 */
	private boolean impenetrable = true;

	/**
	 * Denotes whether this object has actions associated with it or not.
	 */
	private boolean interactive;

	/**
	 * Denotes whether or not this object obstructs the ground.
	 */
	private boolean obstructive;

	/**
	 * This object's length.
	 */
	private int length = 1;

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
	private boolean solid = true;

	/**
	 * This object's width.
	 */
	private int width = 1;

	private int[] types;
	private int[][] models;
	private int height;
	private int collisionType;
	private int adjustValue;
	private boolean dynamicShading;
	private byte occludes;
	private int decorationDisplacement;
	private int contrast;
	private int ambience;
	private short[] originalColours;
	private short[] replacementColours;
	private short[] originalTextures;
	private short[] replacementTextures;
	private boolean mirrorModel;
	private boolean castsShadow;
	private int scaleX;
	private int scaleY;
	private int scaleZ;
	private int mapscene;
	private int rotationFlag;
	private int translateX;
	private int translateY;
	private int translateZ;
	private boolean decoration;
	private int holdsItemPiles;
	private int varbit;
	private int varp;
	private int[] morphisms;
	private int f2117;
	private int f2118;
	private int f2119;
	private int f2120;
	private int[] f2121;
	private boolean members;
	private int mapSceneType;
	private int[] animations;
	private Int2ObjectMap<Object> parameters;

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
	 * Gets the id of this object.
	 *
	 * @return The id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the length of this object.
	 *
	 * @return The length.
	 */
	public int getLength() {
		return length;
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
	 * Indicates whether or not this object obstructs the ground.
	 *
	 * @return {@code true} if the object obstructs the ground otherwise {@code false}.
	 */
	public boolean isObstructive() {
		return obstructive;
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
	 * Sets the length of this object.
	 *
	 * @param length The length.
	 */
	public void setLength(int length) {
		this.length = length;
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

	/**
	 * Sets whether or not this object is obstructive to the ground.
	 *
	 * @param obstructive Whether or not this object obstructs the ground.
	 */
	public void setObstructive(boolean obstructive) {
		this.obstructive = obstructive;
	}

	public int[] getTypes() {
		return types;
	}

	public void setTypes(int[] types) {
		this.types = types;
	}

	public int[][] getModels() {
		return models;
	}

	public void setModels(int[][] models) {
		this.models = models;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getCollisionType() {
		return collisionType;
	}

	public void setCollisionType(int collisionType) {
		this.collisionType = collisionType;
	}

	public int getAdjustValue() {
		return adjustValue;
	}

	public void setAdjustValue(int adjustValue) {
		this.adjustValue = adjustValue;
	}

	public boolean isDynamicShading() {
		return dynamicShading;
	}

	public void setDynamicShading(boolean dynamicShading) {
		this.dynamicShading = dynamicShading;
	}

	public byte getOccludes() {
		return occludes;
	}

	public void setOccludes(byte occludes) {
		this.occludes = occludes;
	}

	public int getDecorationDisplacement() {
		return decorationDisplacement;
	}

	public void setDecorationDisplacement(int decorationDisplacement) {
		this.decorationDisplacement = decorationDisplacement;
	}

	public int getContrast() {
		return contrast;
	}

	public void setContrast(int contrast) {
		this.contrast = contrast;
	}

	public int getAmbience() {
		return ambience;
	}

	public void setAmbience(int ambience) {
		this.ambience = ambience;
	}

	public short[] getOriginalColours() {
		return originalColours;
	}

	public void setOriginalColours(short[] originalColours) {
		this.originalColours = originalColours;
	}

	public short[] getReplacementColours() {
		return replacementColours;
	}

	public void setReplacementColours(short[] replacementColours) {
		this.replacementColours = replacementColours;
	}

	public short[] getOriginalTextures() {
		return originalTextures;
	}

	public void setOriginalTextures(short[] originalTextures) {
		this.originalTextures = originalTextures;
	}

	public short[] getReplacementTextures() {
		return replacementTextures;
	}

	public void setReplacementTextures(short[] replacementTextures) {
		this.replacementTextures = replacementTextures;
	}

	public boolean isMirrorModel() {
		return mirrorModel;
	}

	public void setMirrorModel(boolean mirrorModel) {
		this.mirrorModel = mirrorModel;
	}

	public boolean isCastsShadow() {
		return castsShadow;
	}

	public void setCastsShadow(boolean castsShadow) {
		this.castsShadow = castsShadow;
	}

	public int getScaleX() {
		return scaleX;
	}

	public void setScaleX(int scaleX) {
		this.scaleX = scaleX;
	}

	public int getScaleY() {
		return scaleY;
	}

	public void setScaleY(int scaleY) {
		this.scaleY = scaleY;
	}

	public int getScaleZ() {
		return scaleZ;
	}

	public void setScaleZ(int scaleZ) {
		this.scaleZ = scaleZ;
	}

	public int getMapscene() {
		return mapscene;
	}

	public void setMapscene(int mapscene) {
		this.mapscene = mapscene;
	}

	public int getRotationFlag() {
		return rotationFlag;
	}

	public void setRotationFlag(int rotationFlag) {
		this.rotationFlag = rotationFlag;
	}

	public int getTranslateX() {
		return translateX;
	}

	public void setTranslateX(int translateX) {
		this.translateX = translateX;
	}

	public int getTranslateY() {
		return translateY;
	}

	public void setTranslateY(int translateY) {
		this.translateY = translateY;
	}

	public int getTranslateZ() {
		return translateZ;
	}

	public void setTranslateZ(int translateZ) {
		this.translateZ = translateZ;
	}

	public boolean isDecoration() {
		return decoration;
	}

	public void setDecoration(boolean decoration) {
		this.decoration = decoration;
	}

	public int getHoldsItemPiles() {
		return holdsItemPiles;
	}

	public void setHoldsItemPiles(int holdsItemPiles) {
		this.holdsItemPiles = holdsItemPiles;
	}

	public int getVarbit() {
		return varbit;
	}

	public void setVarbit(int varbit) {
		this.varbit = varbit;
	}

	public int getVarp() {
		return varp;
	}

	public void setVarp(int varp) {
		this.varp = varp;
	}

	public int[] getMorphisms() {
		return morphisms;
	}

	public void setMorphisms(int[] morphisms) {
		this.morphisms = morphisms;
	}

	public int getF2117() {
		return f2117;
	}

	public void setF2117(int f2117) {
		this.f2117 = f2117;
	}

	public int getF2118() {
		return f2118;
	}

	public void setF2118(int f2118) {
		this.f2118 = f2118;
	}

	public int getF2119() {
		return f2119;
	}

	public void setF2119(int f2119) {
		this.f2119 = f2119;
	}

	public int getF2120() {
		return f2120;
	}

	public void setF2120(int f2120) {
		this.f2120 = f2120;
	}

	public int[] getF2121() {
		return f2121;
	}

	public void setF2121(int[] f2121) {
		this.f2121 = f2121;
	}

	public boolean isMembers() {
		return members;
	}

	public void setMembers(boolean members) {
		this.members = members;
	}

	public int getMapSceneType() {
		return mapSceneType;
	}

	public void setMapSceneType(int mapSceneType) {
		this.mapSceneType = mapSceneType;
	}

	public int[] getAnimations() {
		return animations;
	}

	public void setAnimations(int[] animations) {
		this.animations = animations;
	}

	public Int2ObjectMap<Object> getParameters() {
		return parameters;
	}

	public void setParameters(Int2ObjectMap<Object> parameters) {
		this.parameters = parameters;
	}
}