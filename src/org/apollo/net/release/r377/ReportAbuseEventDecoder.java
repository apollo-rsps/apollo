package org.apollo.net.release.r377;

import org.apollo.game.event.impl.ReportAbuseEvent;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketReader;
import org.apollo.net.release.EventDecoder;

/**
 * An {@link EventDecoder} for {@link ReportAbuseEvent}s.
 *
 * @author Kyle Stevenson
 */
public class ReportAbuseEventDecoder extends EventDecoder<ReportAbuseEvent> {

	@Override
	public ReportAbuseEvent decode(GamePacket packet) {
		GamePacketReader reader = new GamePacketReader(packet);

		long encodedUsername = reader.getSigned(DataType.LONG);
		byte ruleBroken = (byte) reader.getUnsigned(DataType.BYTE);
		byte muteFor48Hours = (byte) (reader.getUnsigned(DataType.BYTE) & 0x1);

		return new ReportAbuseEvent(encodedUsername, ruleBroken, muteFor48Hours);
	}

}