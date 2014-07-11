package org.apollo.net.release.r317;

import org.apollo.game.event.impl.UpdateRunEnergyEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.release.EventEncoder;

/**
 * An {@link EventEncoder} for the {@link UpdateRunEnergyEvent}
 * 
 * @author Major
 */
public final class UpdateRunEnergyEventEncoder extends EventEncoder<UpdateRunEnergyEvent> {

	@Override
	public GamePacket encode(UpdateRunEnergyEvent event) {
		GamePacketBuilder builder = new GamePacketBuilder(110);
		builder.put(DataType.BYTE, event.getEnergy());
		return builder.toGamePacket();
	}

}