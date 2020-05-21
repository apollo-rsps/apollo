package org.apollo.game.release.r181.encoders.region;

import org.apollo.game.message.impl.encode.UpdateZonePartialEnclosedMessage;
import org.apollo.game.message.impl.encode.RegionUpdateMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;
import org.apollo.net.release.Release;

/**
 * A {@link MessageEncoder} for the {@link UpdateZonePartialEnclosedMessage}.
 *
 * @author Major
 */
public final class UpdateZonePartialEnclosedEncoder extends MessageEncoder<UpdateZonePartialEnclosedMessage> {

	/**
	 * The Release containing the MessageEncoders for the RegionUpdateMessages.
	 */
	private final Release release;

	/**
	 * Creates the GroupedRegionUpdateMessageEncoder.
	 *
	 * @param release The {@link Release} containing the {@link MessageEncoder}s for the {@link RegionUpdateMessage}s.
	 */
	public UpdateZonePartialEnclosedEncoder(Release release) {
		this.release = release;
	}

	@Override
	public GamePacket encode(UpdateZonePartialEnclosedMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(17, PacketType.VARIABLE_SHORT);
		Position base = message.getLastKnownRegion(), region = message.getRegionPosition();

		builder.put(DataType.BYTE, DataTransformation.SUBTRACT, region.getLocalY(base));
		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalX(base));

		for (RegionUpdateMessage update : message.getMessages()) {
			@SuppressWarnings("unchecked")
			MessageEncoder<RegionUpdateMessage> encoder = (MessageEncoder<RegionUpdateMessage>) release
				.getMessageEncoder(update.getClass());

			GamePacket packet = encoder.encode(update);
			builder.put(DataType.BYTE, update.priority());
			builder.putBytes(packet.content());
		}

		return builder.toGamePacket();
	}

}