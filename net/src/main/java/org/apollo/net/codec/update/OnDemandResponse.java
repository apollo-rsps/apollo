package org.apollo.net.codec.update;

import io.netty.buffer.ByteBuf;
import org.apollo.net.codec.update.OnDemandRequest.Priority;

/**
 * Represents a single 'on-demand' response.
 *
 * @author Graham
 */
public final class OnDemandResponse {

	/**
	 * The chunk data.
	 */
	private final ByteBuf chunkData;

	private final int fs;
	private final int folder;
	private final Priority priority;

	/**
	 * Creates the 'on-demand' response.
	 *
	 * @param fs        The file descriptor.
	 * @param chunkData The chunk data.
	 */
	public OnDemandResponse(int fs, int folder, Priority priority, ByteBuf chunkData) {
		this.fs = fs;
		this.folder = folder;
		this.priority = priority;
		this.chunkData = chunkData;
	}

	/**
	 * Gets the chunk data.
	 *
	 * @return The chunk data.
	 */
	public ByteBuf getChunkData() {
		return chunkData;
	}

	public int getFs() {
		return fs;
	}

	public int getFolder() {
		return folder;
	}

	public Priority getPriority() {
		return priority;
	}
}