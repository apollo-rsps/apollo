package org.apollo.net.release;

import java.util.HashMap;
import java.util.Map;

import org.apollo.game.event.Event;
import org.apollo.net.meta.PacketMetaData;
import org.apollo.net.meta.PacketMetaDataGroup;

/**
 * A {@link Release} is a distinct client version, for example 317 is a common
 * release used in server emulators.
 * @author Graham
 */
public abstract class Release {

	/**
	 * The release number, e.g. {@code 317}.
	 */
	private final int releaseNumber;

	/**
	 * The decoders.
	 */
	private final EventDecoder<?>[] decoders = new EventDecoder<?>[256];

	/**
	 * The encoders.
	 */
	private final Map<Class<? extends Event>, EventEncoder<?>> encoders = new HashMap<Class<? extends Event>, EventEncoder<?>>();

	/**
	 * The incoming packet meta data.
	 */
	private final PacketMetaDataGroup incomingPacketMetaData;

	/**
	 * Creates the release.
	 * @param releaseNumber The release number.
	 * @param incomingPacketMetaData The incoming packet meta data.
	 */
	public Release(int releaseNumber, PacketMetaDataGroup incomingPacketMetaData) {
		this.releaseNumber = releaseNumber;
		this.incomingPacketMetaData = incomingPacketMetaData;
	}

	/**
	 * Gets the release number.
	 * @return The release number.
	 */
	public final int getReleaseNumber() {
		return releaseNumber;
	}

	/**
	 * Registers a {@link EventDecoder} for the specified opcode.
	 * @param opcode The opcode, between 0 and 255 inclusive.
	 * @param decoder The {@link EventDecoder}.
	 */
	public final <E extends Event> void register(int opcode, EventDecoder<E> decoder) {
		if (opcode < 0 || opcode >= decoders.length) {
			throw new IndexOutOfBoundsException();
		}
		decoders[opcode] = decoder;
	}

	/**
	 * Registers a {@link EventEncoder} for the specified event type.
	 * @param type The event type.
	 * @param encoder The {@link EventEncoder}.
	 */
	public final <E extends Event> void register(Class<E> type, EventEncoder<E> encoder) {
		encoders.put(type, encoder);
	}

	/**
	 * Gets meta data for the specified incoming packet.
	 * @param opcode The opcode of the incoming packet.
	 * @return The {@link PacketMetaData} object.
	 */
	public final PacketMetaData getIncomingPacketMetaData(int opcode) {
		return incomingPacketMetaData.getMetaData(opcode);
	}

	/**
	 * Gets the {@link EventDecoder} for the specified opcode.
	 * @param opcode The opcode.
	 * @return The {@link EventDecoder}.
	 */
	public final EventDecoder<?> getEventDecoder(int opcode) {
		if (opcode < 0 || opcode >= decoders.length) {
			throw new IndexOutOfBoundsException();
		}
		return decoders[opcode];
	}

	/**
	 * Gets an {@link EventEncoder} for the specified event type.
	 * @param type The type of event.
	 * @return The {@link EventEncoder}.
	 */
	@SuppressWarnings("unchecked")
	public <E extends Event> EventEncoder<E> getEventEncoder(Class<E> type) {
		return (EventEncoder<E>) encoders.get(type);
	}

}
