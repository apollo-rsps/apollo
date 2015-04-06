package org.apollo.net.release.r317;

import org.apollo.game.message.impl.GroupedRegionUpdateMessage;
import org.apollo.game.message.impl.RegionUpdateMessage;
import org.apollo.game.model.Position;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;
import org.apollo.net.release.Release;

/**
 * A {@link MessageEncoder} for the {@link GroupedRegionUpdateMessage}.
 *
 * @author Major
 */
public final class GroupedRegionUpdateMessageEncoder extends MessageEncoder<GroupedRegionUpdateMessage> {

	/**
	 * The Release containing the MessageEncoders for the RegionUpdateMessages.
	 */
	private final Release release;

	/**
	 * Creates the GroupedRegionUpdateMessageEncoder.
	 *
	 * @param release The {@link Release} containing the {@link MessageEncoder}s for the {@link RegionUpdateMessage}s.
	 */
	public GroupedRegionUpdateMessageEncoder(Release release) {
		this.release = release;
	}

	@Override
	public GamePacket encode(GroupedRegionUpdateMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(60, PacketType.VARIABLE_SHORT);
		Position lastKnownRegion = message.getLastKnownRegion(), region = message.getRegionPosition();

		builder.put(DataType.BYTE, region.getLocalY(lastKnownRegion));
		builder.put(DataType.BYTE, DataTransformation.NEGATE, region.getLocalX(lastKnownRegion));
        System.out.println("Grum: local x: " + lastKnownRegion.getLocalX(region) + ", local y: " + lastKnownRegion.getLocalY(region));

		for (RegionUpdateMessage update : message.getMessages()) {
			System.out.println("==== Sending " + update + " as part of grum");
			@SuppressWarnings("unchecked")
			MessageEncoder<RegionUpdateMessage> encoder = (MessageEncoder<RegionUpdateMessage>) release.getMessageEncoder(update.getClass());

			GamePacket packet = encoder.encode(update);
			builder.put(DataType.BYTE, packet.getOpcode());
			builder.putBytes(packet.getPayload());
		}

		return builder.toGamePacket();
	}

}