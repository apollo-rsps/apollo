package org.apollo.cache.def;

import com.google.common.base.Preconditions;
import org.apollo.cache.decoder.rsenum.ScriptVarType;

import java.util.HashMap;
import java.util.Map;

public final class EnumDefinition {

	private static final Map<Integer, EnumDefinition> definitions = new HashMap<>();

	/**
	 * Gets the total number of enum definitions.
	 *
	 * @return The count.
	 */
	public static int count() {
		return definitions.size();
	}

	/**
	 * Initialises the enum definitions.
	 *
	 * @param definitions The definitions.
	 * @throws RuntimeException If there is an id mismatch.
	 */
	public static void init(EnumDefinition[] definitions) {
		for (int id = 0; id < definitions.length; id++) {
			EnumDefinition def = definitions[id];
			if (def != null) {
				if (def.getId() != id) {
					throw new RuntimeException("Equipment definition id mismatch.");
				}
				EnumDefinition.definitions.put(def.getId(), def);
			}
		}
	}

	/**
	 * Gets an enum definition by its id.
	 *
	 * @param id The id.
	 * @return {@code null} if the item is not enum, the definition otherwise.
	 * @throws IndexOutOfBoundsException If the id is out of bounds.
	 */
	public static EnumDefinition lookup(int id) {
		Preconditions.checkElementIndex(id, EnumDefinition.count(), "Id out of bounds.");
		return definitions.get(id);
	}

	/**
	 * The enum id.
	 */
	private final int id;

	private ScriptVarType keyType = ScriptVarType.INTEGER;
	private ScriptVarType valType = ScriptVarType.INTEGER;
	private int defaultInt = 0;
	private String defaultString = "";
	private HashMap<Integer, Integer> intValues;
	private HashMap<Integer, String> stringValues;

	/**
	 * Creates a new enum definition.
	 *
	 * @param id The id.
	 */
	public EnumDefinition(int id) {
		this.id = id;
	}

	public int lookup(int id, int defaultInt) {
		if (intValues == null) {
			return id;
		}
		return intValues.getOrDefault(id, id);
	}

	public String lookup(int id, String defaultString) {
		if (stringValues == null) {
			return defaultString;
		}
		return stringValues.getOrDefault(id, defaultString);
	}

	public int getId() {
		return id;
	}

	public ScriptVarType getKeyType() {
		return keyType;
	}

	public void setKeyType(ScriptVarType keyType) {
		this.keyType = keyType;
	}

	public ScriptVarType getValType() {
		return valType;
	}

	public void setValType(ScriptVarType valType) {
		this.valType = valType;
	}

	public int getDefaultInt() {
		return defaultInt;
	}

	public void setDefaultInt(int defaultInt) {
		this.defaultInt = defaultInt;
	}

	public String getDefaultString() {
		return defaultString;
	}

	public void setDefaultString(String defaultString) {
		this.defaultString = defaultString;
	}

	public HashMap<Integer, Integer> getIntValues() {
		return intValues;
	}

	public void setIntValues(HashMap<Integer, Integer> intValues) {
		this.intValues = intValues;
	}

	public HashMap<Integer, String> getStringValues() {
		return stringValues;
	}

	public void setStringValues(HashMap<Integer, String> stringValues) {
		this.stringValues = stringValues;
	}
}

