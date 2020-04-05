package org.apollo.game.release.r181.encoders.player;

import org.apollo.cache.def.EquipmentDefinition;
import org.apollo.game.message.impl.PlayerSynchronizationMessage;
import org.apollo.game.model.Animation;
import org.apollo.game.model.Appearance;
import org.apollo.game.model.Direction;
import org.apollo.game.model.Graphic;
import org.apollo.game.model.Item;
import org.apollo.game.model.Position;
import org.apollo.game.model.entity.EquipmentConstants;
import org.apollo.game.model.entity.setting.Gender;
import org.apollo.game.model.inv.Inventory;
import org.apollo.game.sync.block.AnimationBlock;
import org.apollo.game.sync.block.AppearanceBlock;
import org.apollo.game.sync.block.ChatBlock;
import org.apollo.game.sync.block.ForceChatBlock;
import org.apollo.game.sync.block.ForceMovementBlock;
import org.apollo.game.sync.block.GraphicBlock;
import org.apollo.game.sync.block.HitUpdateBlock;
import org.apollo.game.sync.block.InteractingMobBlock;
import org.apollo.game.sync.block.SecondaryHitUpdateBlock;
import org.apollo.game.sync.block.SynchronizationBlockSet;
import org.apollo.game.sync.block.TurnToPositionBlock;
import org.apollo.game.sync.seg.player.AddHighResolutionPlayer;
import org.apollo.game.sync.seg.MovementSegment;
import org.apollo.game.sync.seg.SegmentType;
import org.apollo.game.sync.seg.SynchronizationSegment;
import org.apollo.game.sync.seg.TeleportSegment;
import org.apollo.net.codec.game.DataOrder;
import org.apollo.net.codec.game.DataTransformation;
import org.apollo.net.codec.game.DataType;
import org.apollo.net.codec.game.GamePacket;
import org.apollo.net.codec.game.GamePacketBuilder;
import org.apollo.net.meta.PacketType;
import org.apollo.net.release.MessageEncoder;

/**
 * A {@link MessageEncoder} for the {@link PlayerSynchronizationMessage}.
 *
 * @author Graham
 * @author Major
 */
public final class PlayerSynchronizationMessageEncoder extends MessageEncoder<PlayerSynchronizationMessage> {

	@Override
	public GamePacket encode(PlayerSynchronizationMessage message) {
		GamePacketBuilder builder = new GamePacketBuilder(90, PacketType.VARIABLE_SHORT);
		builder.switchToBitAccess();

		GamePacketBuilder blockBuilder = new GamePacketBuilder();


		//low-rez-movement
		//low-rez-

		// high-rez-movement

		return builder.toGamePacket();
	}
}