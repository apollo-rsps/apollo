package org.apollo.cache.map;

import org.apollo.cache.Group;

import java.lang.ref.WeakReference;
import java.util.Collections;
import java.util.Map;

/**
 * A definition for a map.
 */
public final class MapIndex {

    /**
     * Indicates whether or not this map is members-only.
     */
    private final boolean members;

    /**
     * The object file id.
     */
    private final WeakReference<Group> landscapeFolder;

    /**
     * The packed coordinates.
     */
    private final int packedCoordinates;

    /**
     * The terrain file id.
     */
    private final WeakReference<Group> terrainFolder;

    /**
     * A mapping of region ids to {@link MapIndex}es.
     */
    private static Map<Integer, MapIndex> indices;

    /**
     * Initialises the class with the specified set of indices.
     */
    public static void init(Map<Integer, MapIndex> indices) {
        MapIndex.indices = Collections.unmodifiableMap(indices);
    }

    /**
     * Gets the {@code Map} of {@link MapIndex} instances.
     *
     * @return The map of {@link MapIndex} instances.
     */
    public static Map<Integer, MapIndex> getIndices() {
        return indices;
    }

    /**
     * Creates the {@link MapIndex}.
     *  @param packedCoordinates The packed coordinates.
     * @param terrainGroup           The terrain file id.
     * @param landscapeGroup           The object file id.
     * @param members           Indicates whether or not this map is members-only.
     */
    public MapIndex(int packedCoordinates, Group terrainGroup, Group landscapeGroup, boolean members) {
        this.packedCoordinates = packedCoordinates;
        this.terrainFolder = new WeakReference<>(terrainGroup);
        this.landscapeFolder = new WeakReference<>(landscapeGroup);
        this.members = members;
    }

    /**
     * Gets the id of the file containing the object data.
     *
     * @return The file id.
     */
    public WeakReference<Group> getLandscapeFolder() {
        return landscapeFolder;
    }

    /**
     * Gets the packed coordinates.
     *
     * @return The packed coordinates.
     */
    public int getPackedCoordinates() {
        return packedCoordinates;
    }

    /**
     * Gets the id of the file containing the terrain data.
     *
     * @return The file id.
     */
    public WeakReference<Group> getTerrainFolder() {
        return terrainFolder;
    }

    /**
     * Gets the X coordinate of this map.
     *
     * @return The X coordinate of this map.
     */
    public int getX() {
        return (packedCoordinates >> 8 & 0xFF) * MapConstants.MAP_WIDTH;
    }

    /**
     * Gets the Y coordinate of this map.
     *
     * @return The y coordinate of this map.
     */
    public int getY() {
        return (packedCoordinates & 0xFF) * MapConstants.MAP_WIDTH;

    }

    /**
     * Returns whether or not this MapIndex is for a members-only area of the world.
     *
     * @return {@code true} if this MapIndex is for a members-only area, {@code false} if not.
     */
    public boolean isMembersOnly() {
        return members;
    }

}
