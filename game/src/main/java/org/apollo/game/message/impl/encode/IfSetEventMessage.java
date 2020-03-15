package org.apollo.game.message.impl.encode;

import kotlin.ranges.IntRange;
import org.apollo.game.model.inter.InterfaceEvent;
import org.apollo.net.message.Message;

import java.util.EnumSet;

/**
 * @author Khaled Abdeljaber
 */
public class IfSetEventMessage extends Message {

	private final int packedInterface;
	private final IntRange range;
	private final EnumSet<InterfaceEvent> events;

	public IfSetEventMessage(int packedInterface, IntRange range, EnumSet<InterfaceEvent> events) {
		this.packedInterface = packedInterface;
		this.range = range;
		this.events = events;
	}

	public int getPackedInterface() {
		return packedInterface;
	}

	public IntRange getRange() {
		return range;
	}

	public EnumSet<InterfaceEvent> getEvents() {
		return events;
	}
}
